package ru.kpfu.itis.lifeTrack.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.kpfu.itis.lifeTrack.dto.request.EventRequestDto;
import ru.kpfu.itis.lifeTrack.dto.response.EventResponseDto;
import ru.kpfu.itis.lifeTrack.model.EventEntity;

import java.util.Set;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface EventMapper {

    @Mapping(source = "creator", target = "creator.id")
    @Mapping(source = "projectId", target = "project.id")
    EventEntity responseDtoToEntity(EventResponseDto responseDto);

    EventEntity requestDtoToEntity(EventRequestDto requestDto);

    EventResponseDto requestDtoToResponseDto(EventRequestDto requestDto);

    EventRequestDto responseDtoToRequestDto(EventResponseDto responseDto);

    @Mapping(source = "creator.id", target = "creator")
    @Mapping(source = "project.id", target = "projectId")
    EventResponseDto entityToResponseDto(EventEntity eventEntity);

    EventRequestDto entityToRequestDto(EventEntity eventEntity);

    Set<EventEntity> responseDtoSetToEntitySet(Set<EventResponseDto> dtoSet);

    Set<EventResponseDto> entitySetToResponseDtoSet(Set<EventEntity> entitySet);
}
