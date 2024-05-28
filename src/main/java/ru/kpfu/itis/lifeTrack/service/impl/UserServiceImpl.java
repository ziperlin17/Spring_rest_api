package ru.kpfu.itis.lifeTrack.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.lifeTrack.dto.response.UserDto;
import ru.kpfu.itis.lifeTrack.model.user.UserEntity;
import ru.kpfu.itis.lifeTrack.exception.user.UserAlreadyExistsException;
import ru.kpfu.itis.lifeTrack.exception.user.UserNotFoundException;
import ru.kpfu.itis.lifeTrack.mapper.UserMapper;
import ru.kpfu.itis.lifeTrack.repository.WorkflowAccessRepo;
import ru.kpfu.itis.lifeTrack.repository.UserRepo;
import ru.kpfu.itis.lifeTrack.service.UserService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserMapper userMapper;
    private final WorkflowAccessRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto insertUser(UserEntity user) throws UserAlreadyExistsException {
        if (userRepo.findUserEntityByUsername(user.getUsername()).isPresent()) {
            log.warn("IN insertUser: user with {} username already exists", user.getUsername());
            throw new UserAlreadyExistsException("A user with this username already exists");
        } else if (userRepo.findUserEntityByEmail(user.getEmail()).isPresent()) {
            log.warn("IN insertUser: user with {} email already exists", user.getEmail());
            throw new UserAlreadyExistsException("A user with this email already exists");
        }

        log.info("IN insertUser - user: {} successfully registered", user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.entityToDto(userRepo.save(user));
    }

    @Override
    public UserDto getUser(String userId) throws UserNotFoundException {
        Optional<UserEntity> userEntity = userRepo.findById(userId);
        if (userEntity.isEmpty()) {
            log.warn("IN getUser: User with {} id was not found", userId);
            throw new UserNotFoundException("User with this id does not exist");
        }

        UserDto out = userMapper.entityToDto(userEntity.get());

        log.info("IN getUser - user with id: {} successfully found", out.getId());

        return out;
    }

    @Override
    public UserDto getUserByUsername(String username) throws UserNotFoundException {
        Optional<UserEntity> userEntity = userRepo.findUserEntityByUsername(username);
        if (userEntity.isEmpty()) {
            log.warn("IN getUser: User with {} username was not found", username);
            throw new UserNotFoundException("User with this id does not exist");
        }

        UserDto out = userMapper.entityToDto(userEntity.get());

        log.info("IN getUser - user with username: {} successfully found", username);

        return out;
    }

    @Override
    @Transactional
    public String deleteUser(String userId) throws UserNotFoundException {
        Optional<UserEntity> optionalUser = userRepo.findById(userId);
        if (optionalUser.isEmpty()) {
            log.warn("IN deleteUser - user with id: {} was not found", userId);
            throw new UserNotFoundException("User with this id does not exist");
        }
        roleRepo.deleteAllByUser(optionalUser.get());
        userRepo.deleteById(userId);

        log.info("IN deleteUser - user with id: {} successfully deleted", userId);

        return userId;
    }

    @Override
    public UserDto patchUser(String userId, JsonPatch jsonPatch) throws UserNotFoundException, JsonPatchException, JsonProcessingException {
        Optional<UserEntity> optionalUser = userRepo.findById(userId);
        if (optionalUser.isEmpty()) {
            log.warn("IN patchUser - user with id: {} was not found", userId);
            throw new UserNotFoundException("User with this id does not exist");
        }
        UserEntity user = optionalUser.get();
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(user, JsonNode.class));

        log.info("IN patchUser - user with id: {} successfully patched", userId);

        return userMapper.entityToDto(userRepo.save(objectMapper.treeToValue(patched, UserEntity.class)));
    }

    @Override
    public UserDto updateUser(String userId, UserEntity updated) throws UserNotFoundException {
        Optional<UserEntity> optionalUser = userRepo.findById(userId);
        if (optionalUser.isEmpty()) {
            log.warn("IN updateUser - user with id: {} was not found", userId);
            throw new UserNotFoundException("User with this id does not exist");
        }
        updated.setId(userId);

        log.info("IN updateUser - user with id: {} was successfully updated", userId);

        return userMapper.entityToDto(userRepo.save(updated));
    }

}
