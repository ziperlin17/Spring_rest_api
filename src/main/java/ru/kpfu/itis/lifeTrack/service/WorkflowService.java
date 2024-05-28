package ru.kpfu.itis.lifeTrack.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import ru.kpfu.itis.lifeTrack.exception.NotFoundException;
import ru.kpfu.itis.lifeTrack.exception.user.UserNotFoundException;
import ru.kpfu.itis.lifeTrack.dto.response.WorkflowDto;

import java.util.Set;

public interface WorkflowService {

    Set<WorkflowDto> getWorkflowList(String userId) throws UserNotFoundException;

    WorkflowDto getWorkflow(String userId, Long id) throws NotFoundException;

    WorkflowDto insertWorkflow(String userId, WorkflowDto request) throws NotFoundException;

    WorkflowDto patchWorkflow(String userId, Long id, JsonPatch jsonPatch) throws NotFoundException, JsonProcessingException, JsonPatchException;

    WorkflowDto updateWorkflow(String userId, Long id, WorkflowDto request) throws NotFoundException;

    Long deleteWorkflow(String userId, Long id) throws NotFoundException;
}
