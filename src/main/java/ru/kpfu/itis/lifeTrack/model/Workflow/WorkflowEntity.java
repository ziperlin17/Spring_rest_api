package ru.kpfu.itis.lifeTrack.model.Workflow;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.*;
import ru.kpfu.itis.lifeTrack.model.ProjectEntity;

import java.util.Objects;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "workflow")
@Hidden
public class WorkflowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gcal_id", length = 254)
    private String gCalId;

    @Column(name = "summary", length = 254)
    private String summary;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "timezone", length = 64)
    private String timezone;

    @Column(name = "color", length = 7)
    private String color;

    @OneToMany(mappedBy = "workflow", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<WorkflowAccessRoleEntity> authorized;

    @OneToMany(mappedBy = "workflow", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ProjectEntity> projects;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkflowEntity that = (WorkflowEntity) o;
        return getId().equals(that.getId()) && Objects.equals(gCalId, that.gCalId) && Objects.equals(getSummary(), that.getSummary()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getTimezone(), that.getTimezone()) && Objects.equals(getColor(), that.getColor()) && Objects.equals(getAuthorized(), that.getAuthorized()) && Objects.equals(getProjects(), that.getProjects());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), gCalId, getSummary(), getDescription(), getTimezone(), getColor());
    }
}
