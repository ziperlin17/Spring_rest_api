package ru.kpfu.itis.lifeTrack.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Table(name = "user_statistics")
public class UserStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(name = "earnings")
    private double earnings;

    @Column(name = "client_count")
    private int clientCount;

    @Column(name = "active_project_time")
    private long activeProjectTime;

    @Column(name = "completed_project_count")
    private int completedProjectCount;

}