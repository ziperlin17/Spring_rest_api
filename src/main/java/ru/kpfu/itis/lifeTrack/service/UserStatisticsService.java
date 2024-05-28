package ru.kpfu.itis.lifeTrack.service;

import ru.kpfu.itis.lifeTrack.exception.NotFoundException;
import ru.kpfu.itis.lifeTrack.model.user.UserStatistics;

import java.util.List;

public interface UserStatisticsService {

    UserStatistics getUserStatistics(Long userId) throws NotFoundException;

    void updateUserStatistics(Long userId, UserStatistics userStatistics) throws NotFoundException;

    void resetUserStatistics(Long userId) throws NotFoundException;

    List<UserStatistics> getAllUserStatistics();
}