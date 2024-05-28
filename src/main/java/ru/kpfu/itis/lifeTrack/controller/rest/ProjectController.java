package ru.kpfu.itis.lifeTrack.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.lifeTrack.dto.request.ProjectRequestDto;
import ru.kpfu.itis.lifeTrack.dto.response.ProjectResponseDto;
import ru.kpfu.itis.lifeTrack.exception.NotFoundException;
import ru.kpfu.itis.lifeTrack.security.jwt.JwtUserDetails;
import ru.kpfu.itis.lifeTrack.service.ProjectService;

import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/workflows/{workflowId}/projects")
@RequiredArgsConstructor
@Tag(name = "Project", description = "Operations related to projects")
@PreAuthorize("isAuthenticated()")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(
            summary = "Get list of projects",
            description = "Returns a list of projects for the specified user and workflow.",
            operationId = "getProjectsList",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of projects retrieved successfully",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProjectResponseDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User, workflow, or projects not found"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized request"
                    )
            }
    )
    @GetMapping("")
    @PreAuthorize("#userDetails.id == #userId")
    public ResponseEntity<Set<ProjectResponseDto>> getProjectsList(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable(name = "userId") String userId,
            @PathVariable(name = "workflowId") Long workflowId) throws NotFoundException {
        Set<ProjectResponseDto> projects = projectService.getProjectList(userId, workflowId);
        return ResponseEntity.ok(projects);
    }

    @Operation(
            summary = "Get project by ID",
            description = "Returns the project with the specified ID for the user and workflow.",
            operationId = "getProject",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Project retrieved successfully",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProjectResponseDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User, workflow, or project not found"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized request"
                    )
            }
    )
    @GetMapping(value = "{projectId}")
    @PreAuthorize("#userDetails.id == #userId")
    public ResponseEntity<ProjectResponseDto> getProject(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable(name = "userId") String userId,
            @PathVariable(name = "workflowId") Long workflowId,
            @PathVariable Long projectId) throws NotFoundException {
        return ResponseEntity.ok(projectService.getProject(userId, workflowId, projectId));
    }

    @Operation(
            summary = "Create a new project",
            description = "Creates a new project for the specified user and workflow.",
            operationId = "insertProject",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Project created successfully",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProjectResponseDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User or workflow not found"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized request"
                    )
            }
    )
    @PostMapping(consumes = {"application/json"})
    @PreAuthorize("#userDetails.id == #userId")
    public ResponseEntity<ProjectResponseDto> insertProject(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable(name = "userId") String userId,
            @PathVariable(name = "workflowId") Long workflowId,
            @RequestBody ProjectRequestDto projectRequest) throws NotFoundException {
        ProjectResponseDto projectDto = projectService.insertProject(userId, workflowId, projectRequest);
        return ResponseEntity.ok(projectDto);
    }
    @Operation(
            summary = "Patch a project",
            description = "Updates a project partially with the provided JSON patch.",
            operationId = "patchProject",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Project updated successfully",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProjectResponseDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User, workflow, or project not found"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized request"
                    )
            }
    )
    @RequestMapping(value = "{projectId}", method = RequestMethod.PATCH, consumes = "application/json")
    @PreAuthorize("#userDetails.id == #userId")
    public ResponseEntity<ProjectResponseDto> patchProject(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable(name = "userId") String userId,
            @PathVariable(name = "workflowId") Long workflowId,
            @PathVariable Long projectId,
            @RequestBody JsonPatch patch) throws JsonPatchException, NotFoundException, JsonProcessingException {
        ProjectResponseDto projectDto = projectService.patchProject(userId, workflowId, projectId, patch);
        return ResponseEntity.ok(projectDto);
    }

    @Operation(
            summary = "Update a project",
            description = "Updates a project with the provided request data.",
            operationId = "updateProject",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Project updated successfully",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProjectResponseDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User, workflow, or project not found"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized request"
                    )
            }
    )
    @PutMapping(value = "{projectId}", consumes = "application/json")
    @PreAuthorize("#userDetails.id == #userId")
    public ResponseEntity<ProjectResponseDto> updateProject(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable(name = "userId") String userId,
            @PathVariable(name = "workflowId") Long workflowId,
            @PathVariable Long projectId,
            @RequestBody ProjectRequestDto projectRequest) throws NotFoundException {
        ProjectResponseDto projectDto = projectService.updateProject(userId, workflowId, projectId, projectRequest);
        return ResponseEntity.ok(projectDto);
    }

    @Operation(
            summary = "Delete a project",
            description = "Deletes a project by its ID.",
            operationId = "deleteProject",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Project deleted successfully",
                            content = {
                                    @Content(
                                            mediaType = "text/plain"
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User, workflow, or project not found"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized request"
                    )
            }
    )
    @DeleteMapping(value = "{projectId}")
    @PreAuthorize("#userDetails.id == #userId")
    public ResponseEntity<?> deleteProject(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable(name = "userId") String userId,
            @PathVariable(name = "workflowId") Long workflowId,
            @PathVariable Long projectId) throws NotFoundException {
        projectService.deleteProject(userId, workflowId, projectId);
        return ResponseEntity.ok().build();
    }
}
