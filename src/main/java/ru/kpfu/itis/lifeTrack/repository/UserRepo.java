package ru.kpfu.itis.lifeTrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.lifeTrack.model.user.UserEntity;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, String> {
    Optional <UserEntity> findUserEntityByUsername(String username);
    Optional <UserEntity> findUserEntityByEmail(String email);
    Optional <UserEntity> findUserEntityByUsernameAndEmail(String username, String email);
}