package ru.kpfu.itis.lifeTrack.model;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kpfu.itis.lifeTrack.model.Workflow.WorkflowEntity;

import java.util.Objects;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project")
@Builder
@Hidden
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "summary", length = 254)
    private String summary;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "color", length = 7)
    private String color;

    @OneToMany(mappedBy = "project", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<EventEntity> eventList;

    @ManyToOne
    @JoinColumn(name = "workflow_id")
    private WorkflowEntity workflow;

    @OneToOne
    @JoinColumn(name = "contract_id")
    private ContractEntity contract;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectEntity project = (ProjectEntity) o;
        return getId().equals(project.getId()) && Objects.equals(getSummary(), project.getSummary()) && Objects.equals(getDescription(), project.getDescription()) && Objects.equals(getColor(), project.getColor()) && Objects.equals(getEventList(), project.getEventList()) && Objects.equals(getWorkflow(), project.getWorkflow());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSummary(), getDescription(), getColor());
    }
}
