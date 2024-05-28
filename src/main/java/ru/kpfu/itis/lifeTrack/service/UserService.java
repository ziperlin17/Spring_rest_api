package ru.kpfu.itis.lifeTrack.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import ru.kpfu.itis.lifeTrack.dto.response.UserDto;
import ru.kpfu.itis.lifeTrack.model.user.UserEntity;
import ru.kpfu.itis.lifeTrack.exception.user.UserAlreadyExistsException;
import ru.kpfu.itis.lifeTrack.exception.user.UserNotFoundException;

public interface UserService {

    UserDto getUser(String userId) throws UserNotFoundException;

    UserDto getUserByUsername(String username) throws UserNotFoundException;

    UserDto insertUser(UserEntity userEntity) throws UserAlreadyExistsException;

    UserDto patchUser(String userId, JsonPatch jsonPatch) throws UserNotFoundException, JsonPatchException, JsonProcessingException;

    UserDto updateUser(String userId, UserEntity updatedUserEntity) throws UserNotFoundException;

    String deleteUser(String userId) throws UserNotFoundException;
}
