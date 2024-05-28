package ru.kpfu.itis.lifeTrack.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.lifeTrack.exception.security.AccessTokenException;
import ru.kpfu.itis.lifeTrack.exception.security.RefreshTokenException;
import ru.kpfu.itis.lifeTrack.model.user.RefreshTokenEntity;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtHelper {

    private final String issuer;

    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    private final Algorithm accessTokenAlgorithm;
    private final Algorithm refreshTokenAlgorithm;

    private final JWTVerifier accessTokenVerifier;
    private final JWTVerifier refreshTokenVerifier;

    public JwtHelper(@Value("${ru.eternitytimeline.access-token-secret}") String accessTokenSecret,
                     @Value("${ru.eternitytimeline.refresh-token-secret}") String refreshTokenSecret,
                     @Value("${ru.eternitytimeline.refresh-token-expiration-days}") int refreshTokenExpirationDays,
                     @Value("${ru.eternitytimeline.access-token-expiration-minutes}") int accessTokenExpirationMinutes,
                     @Value("${ru.eternitytimeline.issuer}") String issuer) {
        accessTokenExpirationMs = (long) accessTokenExpirationMinutes * 60 * 1000;
        refreshTokenExpirationMs = (long) refreshTokenExpirationDays * 24 * 60 * 60 * 1000;

        accessTokenAlgorithm = Algorithm.HMAC256(accessTokenSecret);
        refreshTokenAlgorithm = Algorithm.HMAC256(refreshTokenSecret);

        this.issuer = issuer;

        accessTokenVerifier = JWT.require(accessTokenAlgorithm)
                .withIssuer(issuer)
                .build();

        refreshTokenVerifier = JWT.require(refreshTokenAlgorithm)
                .withIssuer(issuer)
                .build();
    }

    public String generateAccessToken(JwtUserDetails userDetails) {
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(userDetails.getId())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + accessTokenExpirationMs))
                .sign(accessTokenAlgorithm);
    }

    public String generateRefreshToken(JwtUserDetails userDetails, RefreshTokenEntity refreshToken) {
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(userDetails.getId())
                .withClaim("tokenId", refreshToken.getId())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + refreshTokenExpirationMs))
                .sign(refreshTokenAlgorithm);
    }

    private Optional<DecodedJWT> decodeAccessToken(String token) {
        try {
            return Optional.of(accessTokenVerifier.verify(token));
        } catch (JWTVerificationException e) {
            log.error("IN decodeAccessToken: Invalid access token: {}", e.getMessage());
        }
        return Optional.empty();
    }

    private Optional<DecodedJWT> decodeRefreshToken(String token) {
        try {
            return Optional.of(refreshTokenVerifier.verify(token));
        } catch (JWTVerificationException e) {
            log.error("IN decodeRefreshToken: Invalid refresh token: {}", e.getMessage());
        }
        return Optional.empty();
    }

    public boolean validateAccessToken(String token) {
        return decodeAccessToken(token).isPresent();
    }

    public boolean validateRefreshToken(String token) {
        return decodeRefreshToken(token).isPresent();
    }

    public String getUserIdFromAccessToken(String token)  throws AccessTokenException {
        DecodedJWT decodedJWT = decodeAccessToken(token).orElseThrow(() -> new AccessTokenException("Access token is not valid"));
        return decodedJWT.getSubject();
    }

    public String getUserIdFromRefreshToken(String token)  throws RefreshTokenException {
        DecodedJWT decodedJWT = decodeRefreshToken(token).orElseThrow(() -> new RefreshTokenException("Refresh token is not valid"));
        return decodedJWT.getSubject();
    }

    public String getTokenIdFromRefreshToken(String token) throws RefreshTokenException {
        DecodedJWT decodedJWT = decodeRefreshToken(token).orElseThrow(() -> new RefreshTokenException("refresh token is not valid"));
        return decodedJWT.getClaim("tokenId").asString();
    }
}
