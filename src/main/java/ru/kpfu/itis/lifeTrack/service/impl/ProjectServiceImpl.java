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
import ru.kpfu.itis.lifeTrack.dto.request.ContractDto;
import ru.kpfu.itis.lifeTrack.dto.request.ProjectRequestDto;
import ru.kpfu.itis.lifeTrack.dto.response.ProjectResponseDto;
import ru.kpfu.itis.lifeTrack.dto.response.WorkflowDto;
import ru.kpfu.itis.lifeTrack.exception.NotFoundException;
import ru.kpfu.itis.lifeTrack.mapper.ProjectMapper;
import ru.kpfu.itis.lifeTrack.model.ClientEntity;
import ru.kpfu.itis.lifeTrack.model.ContractEntity;
import ru.kpfu.itis.lifeTrack.model.Workflow.WorkflowAccessRoleEntity;
import ru.kpfu.itis.lifeTrack.model.Workflow.WorkflowEntity;
import ru.kpfu.itis.lifeTrack.model.ProjectEntity;
import ru.kpfu.itis.lifeTrack.model.user.UserEntity;
import ru.kpfu.itis.lifeTrack.repository.*;
import ru.kpfu.itis.lifeTrack.service.ContractService;
import ru.kpfu.itis.lifeTrack.service.ProjectService;
import ru.kpfu.itis.lifeTrack.service.WorkflowService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    private final ProjectRepo projectRepo;
    private final WorkflowService workflowService;
    private final WorkflowAccessRepo roleRepo;
    private final ContractRepository contractRepository;
    private final ClientRepository clientRepository;
    private final UserRepo userRepo;
    ContractService contractService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Set<ProjectResponseDto> getProjectList(String userId) throws NotFoundException {
        Set<WorkflowDto> workflowSet = workflowService.getWorkflowList(userId);
        Set<ProjectResponseDto> projectDtoSet = new HashSet<>();
        for (WorkflowDto workflow : workflowSet) {
            projectDtoSet.addAll(projectMapper.entitySetToResponseDtoSet(projectRepo.findAllByWorkflowId(workflow.getId())));
        }
        return projectDtoSet;
    }

    @Override
    public Set<ProjectResponseDto> getProjectList(String userId, Long workflowId) throws NotFoundException {
        return new HashSet<>(projectMapper.entitySetToResponseDtoSet(projectRepo.findAllByWorkflowId(workflowId)));
    }

    @Override
    public ProjectResponseDto getProject(String userId, Long workflowId, Long projectId) throws NotFoundException {
        return projectMapper.entityToResponseDto(projectRepo.findByWorkflowIdAndId(workflowId, projectId).orElseThrow(() -> new NotFoundException("Relation between this workflow and this project does not exists")));
    }

    @Override
    public ProjectResponseDto insertProject(String userId, Long workflowId, ProjectRequestDto projectDto) throws NotFoundException {
        ProjectEntity projectEntity = projectMapper.requestDtoToEntity(projectDto);
        WorkflowEntity workflow = roleRepo.findByUserIdAndWorkflowId(userId, workflowId).orElseThrow(() -> new NotFoundException("Relation between User and workflow was not found")).getWorkflow();
        projectEntity.setWorkflow(workflow);
//        contractService.createContract(ContractDto.builder()
//                .clientId().build())
        ClientEntity clientEntity = new ClientEntity();
        ClientEntity savedClient = clientRepository.save(clientEntity);
        ContractEntity contractEntity = new ContractEntity();
        contractEntity.setCreatedDate(Date.valueOf(LocalDate.now()));
        contractEntity.setClient(savedClient);
        UserEntity currentUser = userRepo.getReferenceById(userId);
        contractEntity.setUser(currentUser);
        ContractEntity savedContract = contractRepository.save(contractEntity);
        projectEntity.setContract(savedContract);
        ProjectEntity savedProject = projectRepo.save(projectEntity);
        return projectMapper.entityToResponseDto(projectRepo.save(projectEntity));
    }

    @Override
    public ProjectResponseDto patchProject(String userId, Long workflowId, Long projectId, JsonPatch patch) throws NotFoundException, JsonPatchException, JsonProcessingException {
        ProjectEntity projectEntity = projectRepo.findByWorkflowIdAndId(workflowId, projectId).orElseThrow(() -> new NotFoundException("Relation between workflow and project not found"));
        JsonNode patched = patch.apply(objectMapper.convertValue(projectEntity, JsonNode.class));

        return projectMapper.entityToResponseDto(projectRepo.save(objectMapper.treeToValue(patched, ProjectEntity.class)));
    }

    @Override
    @Transactional
    public ProjectResponseDto updateProject(String userId, Long workflowId, Long projectId, ProjectRequestDto update) throws NotFoundException {
        ProjectEntity updated = projectMapper.requestDtoToEntity(update);
        updated.setId(projectId);
        WorkflowAccessRoleEntity workflowAccessRoleEntity = roleRepo.findByUserIdAndWorkflowId(userId, workflowId).orElseThrow(() -> new NotFoundException("Relation between Workflow and repo was not found"));
        updated.setWorkflow(workflowAccessRoleEntity.getWorkflow());
        return projectMapper.entityToResponseDto(projectRepo.save(updated));
    }

    @Override
    public Long deleteProject(String userId, Long workflowId, Long projectId) throws NotFoundException {
        ProjectEntity projectEntity = projectRepo.findByWorkflowIdAndId(workflowId, projectId).orElseThrow(() -> new NotFoundException("Relation between workflow and project not found"));
        projectRepo.delete(projectEntity);
        return projectId;
    }
}
