    package ru.kpfu.itis.lifeTrack.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.lifeTrack.model.user.UserEntity;
import ru.kpfu.itis.lifeTrack.model.user.UserStatistics;

import java.util.Optional;

public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long> {
    UserStatistics findUserStatisticsByUser(UserEntity user_id);

    Optional<Object> findUserStatisticsByUser(Optional<UserEntity> byId);
}
