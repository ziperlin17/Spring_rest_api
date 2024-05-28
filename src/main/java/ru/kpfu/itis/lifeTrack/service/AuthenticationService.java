package ru.kpfu.itis.lifeTrack.service;

import ru.kpfu.itis.lifeTrack.dto.security.LoginDto;
import ru.kpfu.itis.lifeTrack.dto.security.SignupDto;
import ru.kpfu.itis.lifeTrack.dto.security.TokenDto;
import ru.kpfu.itis.lifeTrack.exception.security.TokenException;

import javax.security.auth.login.LoginException;

public interface AuthenticationService {

    TokenDto login(LoginDto loginDto) throws LoginException;

    TokenDto signup(SignupDto signupDto) throws LoginException;

    String logout(TokenDto tokenDto) throws TokenException;

    String logoutAll(TokenDto tokenDto) throws TokenException;

    TokenDto accessToken(TokenDto tokenDto) throws TokenException;

    TokenDto refreshToken(TokenDto tokenDto) throws TokenException;

}
