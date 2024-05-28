package ru.kpfu.itis.lifeTrack.dto.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenDto {
    private String userId;
    private String accessToken;
    private String refreshToken;
}
