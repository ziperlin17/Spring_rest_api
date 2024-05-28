package ru.kpfu.itis.lifeTrack.exception.security;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class TokenException extends JWTVerificationException {
    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
