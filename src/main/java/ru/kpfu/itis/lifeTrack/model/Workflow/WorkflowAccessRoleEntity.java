package ru.kpfu.itis.lifeTrack.model.Workflow;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.*;
import ru.kpfu.itis.lifeTrack.model.user.UserEntity;

@Entity
@EqualsAndHashCode
@Data
@AllArgsConstructor
@Table(name = "workflow_user_access_role")
@Hidden
public class WorkflowAccessRoleEntity {

    @EmbeddedId
    private WorkflowRoleEntityId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @MapsId("workflowId")
    @JoinColumn(name = "workflow_id")
    private WorkflowEntity workflow;

    @Enumerated(EnumType.ORDINAL)
    private WorkflowRole role;

    public WorkflowAccessRoleEntity(UserEntity userEntity, WorkflowEntity workflowEntity, WorkflowRole role) {
        this.id = new WorkflowRoleEntityId(userEntity.getId(), workflowEntity.getId());
        this.user = userEntity;
        this.workflow = workflowEntity;
        this.role = role;
    }

    public WorkflowAccessRoleEntity() {
        this.id = new WorkflowRoleEntityId();
    }

    public WorkflowRoleEntityId getId() {
        return id;
    }

    public void setId(WorkflowRoleEntityId id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.id.setUserId(user.getId());
        this.user = user;
    }

    public WorkflowEntity getWorkflow() {
        return workflow;
    }

    public void setWorkflow(WorkflowEntity workflow) {
        this.id.setWorkflowId(workflow.getId());
        this.workflow = workflow;
    }

    public WorkflowRole getRole() {
        return role;
    }

    public void setRole(WorkflowRole role) {
        this.role = role;
    }
}
