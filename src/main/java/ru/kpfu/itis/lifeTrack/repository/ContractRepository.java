package ru.kpfu.itis.lifeTrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.lifeTrack.model.ContractEntity;

import java.util.List;


@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long> {
    List<ContractEntity> findByUserId(String user_id);

    List<ContractEntity> findByClientId(Long clientId);

    ContractEntity findByName(String name);

    List<ContractEntity> findByDescriptionContaining(String keyword);

}