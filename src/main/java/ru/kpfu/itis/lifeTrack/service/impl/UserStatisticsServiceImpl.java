package ru.kpfu.itis.lifeTrack.service.impl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.lifeTrack.exception.NotFoundException;
import ru.kpfu.itis.lifeTrack.model.user.UserStatistics;
import ru.kpfu.itis.lifeTrack.repository.UserRepo;
import ru.kpfu.itis.lifeTrack.repository.UserStatisticsRepository;
import ru.kpfu.itis.lifeTrack.service.UserStatisticsService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStatisticsServiceImpl implements UserStatisticsService {

    private final UserStatisticsRepository userStatisticsRepository;
    private final UserRepo userRepo;


    @Override
    public UserStatistics getUserStatistics(Long userId) throws NotFoundException {
        return (UserStatistics) userStatisticsRepository.findUserStatisticsByUser(userRepo.findById(String.valueOf(userId)))
                .orElseThrow(() -> new NotFoundException("User statistics not found for user ID: " + userId));
    }

    @Override
    public void updateUserStatistics(Long userId, UserStatistics userStatistics) throws NotFoundException {
        UserStatistics existingUserStatistics = getUserStatistics(userId);
        existingUserStatistics.setEarnings(userStatistics.getEarnings());
        existingUserStatistics.setClientCount(userStatistics.getClientCount());
        existingUserStatistics.setActiveProjectTime(userStatisticsRepository.getReferenceById(userId).getActiveProjectTime()
                + userStatistics.getActiveProjectTime());

        userStatisticsRepository.save(existingUserStatistics);
    }

    @Override
    public void resetUserStatistics(Long userId) throws NotFoundException {
        UserStatistics userStatistics = getUserStatistics(userId);
        userStatisticsRepository.delete(userStatistics);
    }

    @Override
    public List<UserStatistics> getAllUserStatistics() {
        return userStatisticsRepository.findAll();
    }
}