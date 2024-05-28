package ru.kpfu.itis.lifeTrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import ru.kpfu.itis.lifeTrack.model.user.UserEntity;

import java.sql.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "contract")
public class ContractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date")
    @CreatedDate
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @OneToOne(mappedBy = "contract", cascade = CascadeType.ALL, optional = false)
    private EarningEntity earning;

    @OneToOne(mappedBy = "contract", cascade = CascadeType.ALL)
    private ProjectEntity project;
}