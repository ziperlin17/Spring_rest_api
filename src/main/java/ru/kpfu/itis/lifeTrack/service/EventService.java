package ru.kpfu.itis.lifeTrack.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import ru.kpfu.itis.lifeTrack.dto.request.EventRequestDto;
import ru.kpfu.itis.lifeTrack.dto.response.EventResponseDto;
import ru.kpfu.itis.lifeTrack.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface EventService {

    Set<EventResponseDto> getEventList(String userId, Long workflowId, Long projectId) throws NotFoundException;

    EventResponseDto getEvent(String userId, Long workflowId, Long projectId, Long eventId) throws NotFoundException;

    EventResponseDto insertEvent(String userId, Long workflowId, Long projectId, EventRequestDto requestDto) throws NotFoundException;

    EventResponseDto moveEvent(String userId, Long workflowId, Long projectId, Long eventId, String destination) throws NotFoundException;

    EventResponseDto patchEvent(String userId, Long workflowId, Long projectId, Long eventId, JsonPatch patch) throws NotFoundException, JsonProcessingException, JsonPatchException;

    EventResponseDto updateEvent(String userId, Long workflowId, Long projectId, Long eventId, EventRequestDto requestDto) throws NotFoundException;

    Long deleteEvent(String userId, Long workflowId, Long projectId, Long eventId) throws NotFoundException;

    EventResponseDto pauseEvent(Long eventId) throws NotFoundException;

    EventResponseDto unpauseEvent(Long eventId) throws NotFoundException;

    List<EventResponseDto> getEventsByDate(LocalDate date) throws NotFoundException;

}
