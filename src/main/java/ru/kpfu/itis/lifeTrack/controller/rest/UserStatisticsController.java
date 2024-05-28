package ru.kpfu.itis.lifeTrack.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.lifeTrack.exception.NotFoundException;
import ru.kpfu.itis.lifeTrack.model.user.UserStatistics;
import ru.kpfu.itis.lifeTrack.service.UserStatisticsService;

import java.util.List;

@RestController
@RequestMapping("/user-statistics")
@Tag(name = "User Statistics", description = "API for managing user statistics")
public class UserStatisticsController {

    private final UserStatisticsService userStatisticsService;

    @Autowired
    public UserStatisticsController(UserStatisticsService userStatisticsService) {
        this.userStatisticsService = userStatisticsService;
    }

    @Operation(
            summary = "Get user statistics by user ID",
            description = "Retrieve user statistics by their user ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User statistics retrieved successfully",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserStatistics.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User statistics not found"
                    )
            }
    )
    @GetMapping("/{userId}")
    public ResponseEntity<UserStatistics> getUserStatistics(@PathVariable Long userId) throws NotFoundException {
        UserStatistics userStatistics = userStatisticsService.getUserStatistics(userId);
        return ResponseEntity.ok(userStatistics);
    }

    @Operation(
            summary = "Update user statistics",
            description = "Update user statistics for the given user ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User statistics updated successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User statistics not found"
                    )
            }
    )
    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUserStatistics(@PathVariable Long userId, @RequestBody UserStatistics userStatistics) throws NotFoundException {
        userStatisticsService.updateUserStatistics(userId, userStatistics);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Reset user statistics",
            description = "Reset user statistics for the given user ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User statistics reset successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User statistics not found"
                    )
            }
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> resetUserStatistics(@PathVariable Long userId) throws NotFoundException {
        userStatisticsService.resetUserStatistics(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get all user statistics",
            description = "Retrieve statistics for all users.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User statistics retrieved successfully",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserStatistics.class)
                                    )
                            }
                    )
            }
    )
    @GetMapping("/all")
    public ResponseEntity<List<UserStatistics>> getAllUserStatistics() {
        List<UserStatistics> allUserStatistics = userStatisticsService.getAllUserStatistics();
        return ResponseEntity.ok(allUserStatistics);
    }
}