package ru.kpfu.itis.lifeTrack.exception.user;

import ru.kpfu.itis.lifeTrack.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
