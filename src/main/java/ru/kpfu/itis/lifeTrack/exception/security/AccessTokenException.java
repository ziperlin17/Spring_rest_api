package ru.kpfu.itis.lifeTrack.exception.security;

public class AccessTokenException extends TokenException{
    public AccessTokenException(String message) {
        super(message);
    }

    public AccessTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
