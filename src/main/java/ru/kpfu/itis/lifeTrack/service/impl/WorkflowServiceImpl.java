package ru.kpfu.itis.lifeTrack.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.lifeTrack.exception.NotFoundException;
import ru.kpfu.itis.lifeTrack.exception.user.UserNotFoundException;
import ru.kpfu.itis.lifeTrack.dto.response.WorkflowDto;
import ru.kpfu.itis.lifeTrack.exception.workflow.WorkflowNotFoundException;
import ru.kpfu.itis.lifeTrack.mapper.WorkflowMapper;
import ru.kpfu.itis.lifeTrack.model.Workflow.WorkflowAccessRoleEntity;
import ru.kpfu.itis.lifeTrack.model.Workflow.WorkflowRole;
import ru.kpfu.itis.lifeTrack.model.user.UserEntity;
import ru.kpfu.itis.lifeTrack.model.Workflow.WorkflowEntity;
import ru.kpfu.itis.lifeTrack.repository.ProjectRepo;
import ru.kpfu.itis.lifeTrack.repository.WorkflowAccessRepo;
import ru.kpfu.itis.lifeTrack.repository.UserRepo;
import ru.kpfu.itis.lifeTrack.repository.WorkflowRepo;
import ru.kpfu.itis.lifeTrack.service.WorkflowService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorkflowServiceImpl implements WorkflowService {

    private final WorkflowRepo workflowRepo;
    private final WorkflowMapper workflowMapper;
    private final WorkflowAccessRepo roleRepo;
    private final UserRepo userRepo;
    private final ProjectRepo projectRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public Set<WorkflowDto> getWorkflowList(String userId) throws UserNotFoundException {
        Optional<UserEntity> userEntity = userRepo.findById(userId);
        if (userEntity.isEmpty()) {
            log.warn("IN getWorkflowList: User with {} id was not found", userId);
            throw new UserNotFoundException("User with this id does not exists");
        }
//          TODO add check that role >= reader
        Set<WorkflowAccessRoleEntity> workflowAccessRoleEntitySet = roleRepo.findAllByUserId(userId);
        Set<WorkflowDto> workflowSet = new HashSet<>();
        for (WorkflowAccessRoleEntity workflowAccessRoleEntity : workflowAccessRoleEntitySet) {
            workflowSet.add(workflowMapper.entityToDto(workflowAccessRoleEntity.getWorkflow()));
        }

        return workflowSet;
    }

    @Override
    public WorkflowDto getWorkflow(String userId, Long id) throws NotFoundException {
        Optional<UserEntity> userEntity = userRepo.findById(userId);
        if (userEntity.isEmpty()) {
            log.warn("IN getWorkflow: User with {} id was not found", userId);
            throw new UserNotFoundException("User with this id does not exists");
        }

        WorkflowAccessRoleEntity workflowAccessRoleEntity = roleRepo.findByUserIdAndWorkflowId(userId, id)
                .orElseThrow(() -> new WorkflowNotFoundException("Workflow with this id does not exists"));
//          TODO add checking that role >= reader

        return workflowMapper.entityToDto(workflowAccessRoleEntity.getWorkflow());
    }

    @Override
    public WorkflowDto insertWorkflow(String userId, WorkflowDto request) throws NotFoundException {
        Optional<UserEntity> optionalUser = userRepo.findById(userId);
        if (optionalUser.isEmpty()) {
            log.warn("IN insertWorkflow: User with {} id was not found", userId);
            throw new UserNotFoundException("User with this id does not exists");
        }

        WorkflowEntity saved = workflowRepo.save(workflowMapper.dtoToEntity(request));
        WorkflowAccessRoleEntity workflowRole = new WorkflowAccessRoleEntity(optionalUser.get(), saved, WorkflowRole.OWNER);
        return workflowMapper.entityToDto(roleRepo.save(workflowRole).getWorkflow());
    }

    @Override
    public WorkflowDto patchWorkflow(String userId, Long id, JsonPatch jsonPatch) throws NotFoundException, JsonProcessingException, JsonPatchException {
        Optional<UserEntity> optionalUser = userRepo.findById(userId);
        if (optionalUser.isEmpty()) {
            log.warn("IN patchWorkflow: User with {} id was not found", userId);
            throw new UserNotFoundException("User with this id does not exists");
        }
        WorkflowAccessRoleEntity workflowAccessRoleEntity = roleRepo.findByUserIdAndWorkflowId(userId, id)
                .orElseThrow(() -> new WorkflowNotFoundException("Workflow with this id does not exists"));
        WorkflowEntity workflowEntity = workflowAccessRoleEntity.getWorkflow();
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(workflowEntity, JsonNode.class));

        return workflowMapper.entityToDto(workflowRepo.save(objectMapper.treeToValue(patched, WorkflowEntity.class)));
    }

    @Override
    public WorkflowDto updateWorkflow(String userId, Long id, WorkflowDto request) throws NotFoundException {
        WorkflowAccessRoleEntity workflowAccessRoleEntity = roleRepo.findByUserIdAndWorkflowId(userId, id)
                .orElseThrow(() -> new NotFoundException("Relation between this user and this workflow does not exists"));

        WorkflowEntity updated = workflowMapper.dtoToEntity(request);
        updated.setId(id);
        return workflowMapper.entityToDto(workflowRepo.save(updated));
    }

    @Override
    @Transactional
    public Long deleteWorkflow(String userId, Long id) throws NotFoundException {
        WorkflowAccessRoleEntity workflowAccessRoleEntity = roleRepo.findByUserIdAndWorkflowId(userId, id)
                .orElseThrow(() -> new NotFoundException("Relation between this user and this workflow does not exists"));
        WorkflowEntity toDelete = workflowAccessRoleEntity.getWorkflow();
        log.info("IN deleteWorkflow workflow with id {} was deleted", toDelete.getId());

        roleRepo.deleteAllByWorkflow(toDelete);
        projectRepo.deleteAllByWorkflow(toDelete);
        workflowRepo.delete(toDelete);
        return id;
    }
}
