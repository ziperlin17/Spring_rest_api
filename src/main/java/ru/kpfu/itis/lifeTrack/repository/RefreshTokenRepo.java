package ru.kpfu.itis.lifeTrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.lifeTrack.model.user.RefreshTokenEntity;
import ru.kpfu.itis.lifeTrack.model.user.UserEntity;

public interface RefreshTokenRepo extends JpaRepository<RefreshTokenEntity, String> {

    void deleteByOwner_Id(String userId);

}
