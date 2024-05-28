package ru.kpfu.itis.lifeTrack.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequestDto {
    private String id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String userPicture;
    private String password;
    private Date createdDate;
    private Date lastUpdatedDate;
}
