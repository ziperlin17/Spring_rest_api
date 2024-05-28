package ru.kpfu.itis.lifeTrack.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "earning")
public class EarningEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private Long amount;

    @OneToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private ContractEntity contract;
}