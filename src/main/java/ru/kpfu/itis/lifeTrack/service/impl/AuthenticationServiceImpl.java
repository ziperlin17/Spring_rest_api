package ru.kpfu.itis.lifeTrack.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.lifeTrack.dto.security.LoginDto;
import ru.kpfu.itis.lifeTrack.dto.security.SignupDto;
import ru.kpfu.itis.lifeTrack.dto.security.TokenDto;
import ru.kpfu.itis.lifeTrack.exception.security.TokenException;
import ru.kpfu.itis.lifeTrack.exception.security.TokenValidationException;
import ru.kpfu.itis.lifeTrack.mapper.UserMapper;
import ru.kpfu.itis.lifeTrack.model.user.RefreshTokenEntity;
import ru.kpfu.itis.lifeTrack.model.user.UserEntity;
import ru.kpfu.itis.lifeTrack.repository.RefreshTokenRepo;
import ru.kpfu.itis.lifeTrack.repository.UserRepo;
import ru.kpfu.itis.lifeTrack.security.jwt.JwtHelper;
import ru.kpfu.itis.lifeTrack.security.jwt.JwtUserDetails;
import ru.kpfu.itis.lifeTrack.security.jwt.JwtUserDetailsService;
import ru.kpfu.itis.lifeTrack.service.AuthenticationService;

import javax.security.auth.login.LoginException;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepo refreshTokenRepo;
    private final UserRepo userRepo;
    private final JwtHelper jwtHelper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUserDetailsService userDetailsService;
    private final UserMapper userMapper;

    @Override
    public TokenDto login(LoginDto loginDto) throws LoginException {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();

            RefreshTokenEntity refreshToken = new RefreshTokenEntity();
            refreshToken.setOwner(userMapper.detailsToEntity(userDetails));
            refreshTokenRepo.save(refreshToken);

            String accessToken = jwtHelper.generateAccessToken(userDetails);
            String refreshTokenString = jwtHelper.generateRefreshToken(userDetails, refreshToken);

            return new TokenDto(userDetails.getId(), accessToken, refreshTokenString);
        } catch (Exception e) {
            log.error("IN login: user with username {} cant login: {}",loginDto.getUsername(), e.getMessage());
            throw new LoginException(e.getMessage());
        }
    }

    @Override
    public TokenDto signup(SignupDto dto) throws LoginException {
        try {
            UserEntity user = new UserEntity();
            user.setUsername(dto.getUsername());
            user.setFirstname(dto.getFirstname());
            user.setLastname(dto.getLastname());
            user.setEmail(dto.getEmail());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setCreatedDate(new java.sql.Date(new Date().getTime()));
            user.setLastUpdatedDate(new java.sql.Date(new Date().getTime()));
            user = userRepo.save(user);
            JwtUserDetails userDetails = userMapper.entityToDetails(user);

            RefreshTokenEntity refreshToken = new RefreshTokenEntity();
            refreshToken.setOwner(user);
            refreshTokenRepo.save(refreshToken);

            String accessToken = jwtHelper.generateAccessToken(userDetails);
            String refreshTokenString = jwtHelper.generateRefreshToken(userDetails, refreshToken);

            return new TokenDto(userDetails.getId(), accessToken, refreshTokenString);
        } catch (Exception e) {
            log.error("IN signup: new user with username {} cant signup: {}", dto.getUsername(), e.getMessage());
            throw new LoginException(e.getMessage());
        }
    }

    @Override
    public String logout(TokenDto dto) throws TokenException {
        try {
            String refreshTokenString = dto.getRefreshToken();
            String tokenId = jwtHelper.getTokenIdFromRefreshToken(refreshTokenString);
            if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepo.existsById(tokenId)) {
                refreshTokenRepo.deleteById(tokenId);
                return dto.getUserId();
            } else {
                log.error("IN logout: log out user with userId {}: Token is not valid or does not exists", dto.getUserId());
                throw new TokenValidationException("Token is not valid or does not exists");
            }
        } catch (TokenException e) {
            log.error("IN logout: TokenException trying to log out user with userId {}: {}", dto.getUserId(), e.getMessage());
            throw new TokenException(e.getMessage());
        }
    }

    @Override
    public String logoutAll(TokenDto dto) throws TokenException {
        try {
            String refreshTokenString = dto.getRefreshToken();
            String tokenId = jwtHelper.getTokenIdFromRefreshToken(refreshTokenString);
            if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepo.existsById(tokenId)) {
                refreshTokenRepo.deleteByOwner_Id(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));
                return dto.getUserId();
            } else {
                log.error("IN logoutAll: log out user with userId {}: Token is not valid or does not exists", dto.getUserId());
                throw new TokenValidationException("Token is not valid or does not exists");
            }
        } catch (TokenException e) {
            log.error("IN logoutAll: TokenException trying to log out user with userId {}: {}", dto.getUserId(), e.getMessage());
            throw new TokenException(e.getMessage());
        }
    }

    @Override
    public TokenDto accessToken(TokenDto dto) throws TokenException {
        try {
            String refreshTokenString = dto.getRefreshToken();
            if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepo.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
                JwtUserDetails user = userDetailsService.findById(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));
                String accessToken = jwtHelper.generateAccessToken(user);

                return new TokenDto(user.getId(), accessToken, refreshTokenString);
            } else {
                log.error("IN accessToken: user with id {}: Token is not valid or does not exists", dto.getUserId());
                throw new TokenValidationException("Token is not valid or does not exists");
            }
        } catch (TokenException e) {
            log.error("IN accessToken: TokenException trying to log out user with userId {}: {}", dto.getUserId(), e.getMessage());
            throw new TokenException(e.getMessage());
        }
    }

    @Override
    public TokenDto refreshToken(TokenDto dto) throws TokenException {
        try {
            String refreshTokenString = dto.getRefreshToken();
            String tokenId = jwtHelper.getTokenIdFromRefreshToken(refreshTokenString);
            if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepo.existsById(tokenId)) {
                refreshTokenRepo.deleteById(tokenId);

                JwtUserDetails user = userDetailsService.findById(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));

                RefreshTokenEntity refreshToken = new RefreshTokenEntity();

                refreshToken.setOwner(userMapper.detailsToEntity(user));
                refreshTokenRepo.save(refreshToken);

                String accessToken = jwtHelper.generateAccessToken(user);
                String newRefreshToken = jwtHelper.generateRefreshToken(user, refreshToken);

                return new TokenDto(user.getId(), accessToken, newRefreshToken);
            } else {
                log.error("IN refreshToken: user with id {}: Token is not valid or does not exists", dto.getUserId());
                throw new TokenValidationException("Token is not valid or does not exists");
            }
        } catch (TokenException e) {
            log.error("IN refreshToken: TokenException trying to log out user with userId {}: {}", dto.getUserId(), e.getMessage());
            throw new TokenException(e.getMessage());
        }
    }
}
