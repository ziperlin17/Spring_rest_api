package ru.kpfu.itis.lifeTrack.model;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.kpfu.itis.lifeTrack.model.user.UserEntity;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@AllArgsConstructor
@Builder
@Table(name = "event")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @Column(name = "gcal_event_id", length = 32)
    private String googleEventId;

    @Column(name = "ical_uid", length = 26)
    private String iCalendarUID;

    @Column(name = "summary", length = 254)
    private String summary;

    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Timestamp created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated")
    @UpdateTimestamp
    private Timestamp updated;

    @ManyToOne
    @JoinColumn(name = "creator")
    private UserEntity creator;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "schedule_start")
    private Timestamp planStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "schedule_end")
    private Timestamp planEnd;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "user_start")
    private Timestamp userStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "user_end")
    private Timestamp userEnd;

    @Column(name = "finished", nullable = false)
    private Boolean finished;

    @Column(name = "recurrence", columnDefinition = "string[]")
    private String[] recurrence;

    @Column(name = "recurring_event_id")
    private Long recurringEventId;

    @Column(name = "color")
    private String color;

    @Column(name = "status")
    private Boolean active;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventEntity event = (EventEntity) o;
        return getId().equals(event.getId()) && Objects.equals(getGoogleEventId(), event.getGoogleEventId()) && Objects.equals(iCalendarUID, event.iCalendarUID) && Objects.equals(getSummary(), event.getSummary()) && Objects.equals(getDescription(), event.getDescription()) && Objects.equals(getCreated(), event.getCreated()) && Objects.equals(getUpdated(), event.getUpdated()) && Objects.equals(getPlanStart(), event.getPlanStart()) && Objects.equals(getPlanEnd(), event.getPlanEnd()) && Objects.equals(getUserStart(), event.getUserStart()) && Objects.equals(getUserEnd(), event.getUserEnd()) && getFinished().equals(event.getFinished()) && Arrays.equals(getRecurrence(), event.getRecurrence()) && Objects.equals(getRecurringEventId(), event.getRecurringEventId()) && Objects.equals(getColor(), event.getColor());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getId(), getGoogleEventId(), iCalendarUID, getSummary(), getDescription(), getCreated(), getUpdated(), getPlanStart(), getPlanEnd(), getUserStart(), getUserEnd(), getFinished(), getRecurringEventId(), getColor());
        result = 31 * result + Arrays.hashCode(getRecurrence());
        return result;
    }

}
