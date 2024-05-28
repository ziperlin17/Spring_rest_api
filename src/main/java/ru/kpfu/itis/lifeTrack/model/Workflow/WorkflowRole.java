package ru.kpfu.itis.lifeTrack.model.Workflow;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public enum WorkflowRole {
    NONE,
    READER,
    WRITER,
    OWNER
}
