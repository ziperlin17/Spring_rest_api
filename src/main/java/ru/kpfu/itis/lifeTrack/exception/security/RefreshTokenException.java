package ru.kpfu.itis.lifeTrack.exception.security;

public class RefreshTokenException extends TokenException {
    public RefreshTokenException(String message) {
        super(message);
    }

    public RefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
