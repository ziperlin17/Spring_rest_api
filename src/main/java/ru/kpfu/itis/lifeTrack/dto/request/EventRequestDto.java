package ru.kpfu.itis.lifeTrack.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventRequestDto {
//    private String googleEventId;
//    private String iCalendarUID;
    private String summary;
    private String description;
//    private Timestamp planStart;
//    private Timestamp planEnd;
//    private Timestamp userStart;
//    private Timestamp userEnd;
//    private Boolean finished;
//    private String[] recurrence;
//    private String color;
}
