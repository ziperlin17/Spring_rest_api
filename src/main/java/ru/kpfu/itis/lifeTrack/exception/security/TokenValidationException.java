package ru.kpfu.itis.lifeTrack.exception.security;

public class TokenValidationException extends TokenException {

    public TokenValidationException(String message) {
        super(message);
    }

    public TokenValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
