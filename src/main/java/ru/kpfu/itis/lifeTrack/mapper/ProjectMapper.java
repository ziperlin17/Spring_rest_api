package ru.kpfu.itis.lifeTrack.mapper;

import org.mapstruct.Mapper;
import ru.kpfu.itis.lifeTrack.dto.request.ProjectRequestDto;
import ru.kpfu.itis.lifeTrack.dto.response.ProjectResponseDto;
import ru.kpfu.itis.lifeTrack.model.ProjectEntity;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectResponseDto entityToResponseDto(ProjectEntity projectEntity);

    ProjectRequestDto responseDtoToRequestDto(ProjectResponseDto responseDto);

    ProjectResponseDto requestDtoToResponseDto(ProjectRequestDto requestDto);

    ProjectEntity requestDtoToEntity(ProjectRequestDto requestDto);

    Set<ProjectResponseDto> entitySetToResponseDtoSet(Set<ProjectEntity> entitySet);

}
