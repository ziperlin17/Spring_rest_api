package ru.kpfu.itis.lifeTrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.lifeTrack.model.ProjectEntity;
import ru.kpfu.itis.lifeTrack.model.Workflow.WorkflowEntity;

import java.util.Optional;
import java.util.Set;

public interface ProjectRepo extends JpaRepository<ProjectEntity, Long> {
    Set<ProjectEntity> findAllByWorkflowId(Long workflowId);

    Optional<ProjectEntity> findByWorkflowIdAndId(Long workflowId, Long projectId);

    void deleteAllByWorkflow(WorkflowEntity workflow);

    void deleteByWorkflow_Id(Long workflowId);

    ProjectEntity findProjectEntityByContract_Id(Long contractId);
}
