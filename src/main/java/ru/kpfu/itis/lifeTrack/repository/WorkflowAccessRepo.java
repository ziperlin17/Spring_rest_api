package ru.kpfu.itis.lifeTrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.lifeTrack.model.Workflow.WorkflowAccessRoleEntity;
import ru.kpfu.itis.lifeTrack.model.user.UserEntity;
import ru.kpfu.itis.lifeTrack.model.Workflow.WorkflowEntity;

import java.util.Optional;
import java.util.Set;

public interface WorkflowAccessRepo extends JpaRepository<WorkflowAccessRoleEntity, Long> {
    Set<WorkflowAccessRoleEntity> findAllByUserId(String userId);
    Set<WorkflowAccessRoleEntity> findAllByWorkflowId(Long workflowId);
    Optional<WorkflowAccessRoleEntity> findByUserIdAndWorkflowId(String userId, Long workflowId);

    void deleteAllByWorkflow(WorkflowEntity workflowEntity);
    void deleteAllByUser(UserEntity userEntity);
}
