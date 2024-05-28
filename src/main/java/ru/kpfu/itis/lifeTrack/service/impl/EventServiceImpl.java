package ru.kpfu.itis.lifeTrack.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.lifeTrack.dto.request.EventRequestDto;
import ru.kpfu.itis.lifeTrack.dto.response.EventResponseDto;
import ru.kpfu.itis.lifeTrack.exception.NotFoundException;
import ru.kpfu.itis.lifeTrack.exception.user.UserNotFoundException;
import ru.kpfu.itis.lifeTrack.mapper.EventMapper;
import ru.kpfu.itis.lifeTrack.model.EventEntity;
import ru.kpfu.itis.lifeTrack.model.ProjectEntity;
import ru.kpfu.itis.lifeTrack.model.user.UserEntity;
import ru.kpfu.itis.lifeTrack.repository.EventRepo;
import ru.kpfu.itis.lifeTrack.repository.ProjectRepo;
import ru.kpfu.itis.lifeTrack.repository.UserRepo;
import ru.kpfu.itis.lifeTrack.service.EventService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventMapper eventMapper;
    private final ProjectRepo projectRepo;
    private final EventRepo eventRepo;
    private final UserRepo userRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Set<EventResponseDto> getEventList(String userId, Long workflowId, Long projectId) throws NotFoundException {
        ProjectEntity projectEntity = projectRepo.findByWorkflowIdAndId(workflowId, projectId).orElseThrow(() -> new NotFoundException("Relation between workflow and project was not found"));
        Set<EventEntity> eventSet = eventRepo.findAllByProject(projectEntity);
        return eventMapper.entitySetToResponseDtoSet(eventSet);
    }

    @Override
    public EventResponseDto getEvent(String userId, Long workflowId, Long projectId, Long eventId) throws NotFoundException {
        ProjectEntity projectEntity = projectRepo.findByWorkflowIdAndId(workflowId, projectId).orElseThrow(() -> new NotFoundException("Relation between workflow and project was not found"));
        EventEntity event = eventRepo.findByProjectAndId(projectEntity, eventId).orElseThrow(() -> new NotFoundException("Event was not found"));
        return eventMapper.entityToResponseDto(event);
    }

    @Override
    public EventResponseDto insertEvent(String userId, Long workflowId, Long projectId, EventRequestDto requestDto) throws NotFoundException {
        ProjectEntity projectEntity = projectRepo.findByWorkflowIdAndId(workflowId, projectId).orElseThrow(() -> new NotFoundException("Relation between workflow and project was not found"));
        UserEntity creator = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("Relation between workflow and project was not found"));

        EventEntity eventEntity = new EventEntity();
        eventEntity.setProject(projectEntity);
        eventEntity.setSummary("New Event");
        eventEntity.setDescription("Event description");
        eventEntity.setPlanStart(Timestamp.valueOf("2024-05-28 11:08:05.802000"));
        eventEntity.setPlanEnd(Timestamp.valueOf("2024-05-28 15:08:05.802000"));
        eventEntity.setColor("grey");
        eventEntity.setCreator(creator);
        eventEntity.setFinished(Boolean.FALSE);
        EventEntity event = eventRepo.saveAndFlush(eventEntity);
        return eventMapper.entityToResponseDto(event);
    }

    @Override
    public EventResponseDto moveEvent(String userId, Long workflowId, Long projectId, Long eventId, String destination) throws NotFoundException {
        return null;
    }

    @Override
    public EventResponseDto patchEvent(String userId, Long workflowId, Long projectId, Long eventId, JsonPatch patch) throws NotFoundException, JsonProcessingException, JsonPatchException {
        ProjectEntity projectEntity = projectRepo.findByWorkflowIdAndId(workflowId, projectId).orElseThrow(() -> new NotFoundException("Relation between workflow and project was not found"));
        EventEntity eventEntity = eventRepo.findByProjectAndId(projectEntity, eventId).orElseThrow(() -> new NotFoundException("Event was not found"));

        JsonNode patched = patch.apply(objectMapper.convertValue(eventEntity, JsonNode.class));
        return eventMapper.entityToResponseDto(eventRepo.save(objectMapper.treeToValue(patched, EventEntity.class)));

    }

    @Override
    public EventResponseDto updateEvent(String userId, Long workflowId, Long projectId, Long eventId, EventRequestDto requestDto) throws NotFoundException {
        ProjectEntity projectEntity = projectRepo.findByWorkflowIdAndId(workflowId, projectId).orElseThrow(() -> new NotFoundException("Relation between workflow and project was not found"));
        EventEntity origin = eventRepo.findByProjectAndId(projectEntity, eventId).orElseThrow(() -> new NotFoundException("Event was not found"));
        EventEntity updated = eventMapper.requestDtoToEntity(requestDto);
        updated.setProject(projectEntity);
        updated.setId(eventId);
        updated.setCreator(origin.getCreator());
        updated.setCreated(origin.getCreated());
        updated.setUpdated(origin.getUpdated());
        return eventMapper.entityToResponseDto(eventRepo.save(updated));
    }

    @Override
    public Long deleteEvent(String userId, Long workflowId, Long projectId, Long eventId) throws NotFoundException {
        ProjectEntity projectEntity = projectRepo.findByWorkflowIdAndId(workflowId, projectId).orElseThrow(() -> new NotFoundException("Relation between workflow and project was not found"));
        EventEntity eventEntity = eventRepo.findByProjectAndId(projectEntity, eventId).orElseThrow(() -> new NotFoundException("Event was not found"));
        eventRepo.delete(eventEntity);
        return eventId;
    }

    @Override
    public EventResponseDto pauseEvent(Long eventId) throws NotFoundException {
        EventEntity event = eventRepo.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        event.setActive(true);
        event = eventRepo.save(event);
        return eventMapper.entityToResponseDto(event);
    }

    @Override
    public EventResponseDto unpauseEvent(Long eventId) throws NotFoundException {
        EventEntity event = eventRepo.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        event.setActive(false);
        event = eventRepo.save(event);
        return eventMapper.entityToResponseDto(event);
    }

    @Override
    public List<EventResponseDto> getEventsByDate(LocalDate date) throws NotFoundException {
        Timestamp startOfDay = Timestamp.valueOf(date.atStartOfDay());
        Timestamp endOfDay = Timestamp.valueOf(date.atTime(23, 59, 59, 999));

        List<EventEntity> events = eventRepo.findEventEntitiesByPlanStartAndPlanEnd(startOfDay, endOfDay);
        if (events.isEmpty()) {
            throw new NotFoundException("No events found for the specified date");
        }
        return events.stream()
                .map(eventMapper::entityToResponseDto)
                .collect(Collectors.toList());
    }
}
