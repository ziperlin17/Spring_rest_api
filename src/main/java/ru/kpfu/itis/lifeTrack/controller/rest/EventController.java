package ru.kpfu.itis.lifeTrack.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.lifeTrack.dto.request.EventRequestDto;
import ru.kpfu.itis.lifeTrack.dto.response.EventResponseDto;
import ru.kpfu.itis.lifeTrack.exception.NotFoundException;
import ru.kpfu.itis.lifeTrack.security.jwt.JwtUserDetails;
import ru.kpfu.itis.lifeTrack.service.EventService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping(value = "/users/{userId}/workflows/{workflowId}/projects/{projectId}/events", produces = { "application/json" })
@RequiredArgsConstructor
@Tag(name = "Event", description = "Operations related to events within projects and workflows")
@PreAuthorize("isAuthenticated()")
public class EventController {

    private final EventService eventService;

    @Operation(
            operationId = "get-event-list",
            summary = "Get a list of events",
            description = "Retrieve a list of events for a specific project and workflow",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved list of events",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EventResponseDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Workflow or project not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request")
            }
    )
    @GetMapping
    @PreAuthorize("#userDetails.id == #userId")
    public ResponseEntity<Set<EventResponseDto>> getEventsList(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                               @Parameter(description = "ID of the user", required = true) @PathVariable(name = "userId") String userId,
                                                               @Parameter(description = "ID of the workflow", required = true) @PathVariable(name = "workflowId") Long workflowId,
                                                               @Parameter(description = "ID of the project", required = true) @PathVariable(name = "projectId") Long projectId) throws NotFoundException {
        Set<EventResponseDto> eventResponseSet = eventService.getEventList(userId, workflowId, projectId);
        return new ResponseEntity<>(eventResponseSet, HttpStatus.OK);
    }

    @Operation(
            operationId = "get-event",
            summary = "Get event details",
            description = "Retrieve the details of a specific event",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved event details",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EventResponseDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Workflow, project, or event not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request")
            }
    )
    @GetMapping("{eventId}")
    @PreAuthorize("#userDetails.id == #userId")
    public ResponseEntity<EventResponseDto> getEvent(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                     @Parameter(description = "ID of the user", required = true) @PathVariable(name = "userId") String userId,
                                                     @Parameter(description = "ID of the workflow", required = true) @PathVariable(name = "workflowId") Long workflowId,
                                                     @Parameter(description = "ID of the project", required = true) @PathVariable(name = "projectId") Long projectId,
                                                     @Parameter(description = "ID of the event", required = true) @PathVariable(name = "eventId") Long eventId) throws NotFoundException {
        return new ResponseEntity<>(eventService.getEvent(userId, workflowId, projectId, eventId), HttpStatus.OK);
    }

    @Operation(
            operationId = "insert-event",
            summary = "Insert a new event",
            description = "Create a new event in a specific project and workflow",
            requestBody = @RequestBody(
                    description = "Details of the event to be created",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EventRequestDto.class),
                            examples = @ExampleObject(
                                    value = "{ \"summary\": \"New Event\", \"description\": \"Event description\", \"planStart\": \"2023-05-27T10:00:00\", \"planEnd\": \"2023-05-27T12:00:00\", \"creatorId\": 1, \"active\": true, \"recurringEventId\": null }"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully created the event",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EventResponseDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Workflow or project not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request")
            }
    )
    @PostMapping(consumes = "application/json")
    @PreAuthorize("#userDetails.id == #userId")
    public ResponseEntity<EventResponseDto> insertEvent(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @Parameter(description = "ID of the user", required = true) @PathVariable(name = "userId") String userId,
            @Parameter(description = "ID of the workflow", required = true) @PathVariable(name = "workflowId") Long workflowId,
            @Parameter(description = "ID of the project", required = true) @PathVariable(name = "projectId") Long projectId,
            @RequestBody EventRequestDto eventRequestDto
    ) throws NotFoundException {
        EventResponseDto eventResponseDto = eventService.insertEvent(userId, workflowId, projectId, eventRequestDto);
        return new ResponseEntity<>(eventResponseDto, HttpStatus.OK);
    }

    @Operation(
            operationId = "move-event",
            summary = "Move an event",
            description = "Move an event to a different destination within the workflow",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully moved the event",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EventResponseDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Workflow, project, event, or destination not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request")
            }
    )
    @PostMapping("{eventId}/move")
    @PreAuthorize("#userDetails.id == #userId")
    public ResponseEntity<EventResponseDto> moveEvent(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @Parameter(description = "ID of the user", required = true) @PathVariable(name = "userId") String userId,
            @Parameter(description = "ID of the workflow", required = true) @PathVariable(name = "workflowId") Long workflowId,
            @Parameter(description = "ID of the project", required = true) @PathVariable(name = "projectId") Long projectId,
            @Parameter(description = "ID of the event", required = true) @PathVariable(name = "eventId") Long eventId,
            @Parameter(description = "Destination to move the event to", required = true) @RequestParam("destination") String destination) throws NotFoundException {
        EventResponseDto responseDto = eventService.moveEvent(userId, workflowId, projectId, eventId, destination);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(
            operationId = "patch-event",
            summary = "Patch an event",
            description = "Apply partial modifications to an event",
            requestBody = @RequestBody(
                    description = "JSON Patch document specifying the changes",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonPatch.class),
                            examples = @ExampleObject(
                                    value = "[{ \"op\": \"replace\", \"path\": \"/name\", \"value\": \"Updated Event Name\" }]"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully patched the event",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EventResponseDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Workflow, project, or event not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request")
            }
    )
    @PatchMapping(value = "{eventId}", consumes = "application/json")
    @PreAuthorize("#userDetails.id == #userId")
    public ResponseEntity<EventResponseDto> patchEvent(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                       @PathVariable(name = "userId") String userId,
                                                       @PathVariable(name = "workflowId") Long workflowId,
                                                       @PathVariable(name = "projectId") Long projectId,
                                                       @PathVariable(name = "eventId") Long eventId,
                                                       @RequestBody JsonPatch patch) throws JsonPatchException, NotFoundException, JsonProcessingException {
        EventResponseDto responseDto = eventService.patchEvent(userId, workflowId, projectId, eventId, patch);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(
            operationId = "update-event",
            summary = "Update an event",
            description = "Replace an existing event with new data",
            requestBody = @RequestBody(
                    description = "Details of the event to be updated",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EventRequestDto.class),
                            examples = @ExampleObject(
                                    value = "{ \"name\": \"Updated Event\", \"description\": \"Updated description\", \"date\": \"2023-05-28\" }"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully updated the event",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EventResponseDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Workflow, project, or event not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request")
            }
    )
    @PutMapping(value = "{eventId}", consumes = "application/json")
    @PreAuthorize("#userDetails.id == #userId")
    public ResponseEntity<EventResponseDto> updateEvent(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                        @PathVariable(name = "userId") String userId,
                                                        @PathVariable(name = "workflowId") Long workflowId,
                                                        @PathVariable(name = "projectId") Long projectId,
                                                        @PathVariable(name = "eventId") Long eventId,
                                                        @RequestBody EventRequestDto requestDto) throws NotFoundException {
        EventResponseDto responseDto = eventService.updateEvent(userId, workflowId, projectId, eventId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(
            operationId = "delete-event",
            summary = "Delete an event",
            description = "Delete a specific event from the project and workflow",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully deleted the event",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(implementation = Long.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Workflow, project, or event not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized request")
            }
    )
    @DeleteMapping("{eventId}")
    @PreAuthorize("#userDetails.id == #userId")
    public ResponseEntity<Long> deleteEvent(@AuthenticationPrincipal JwtUserDetails userDetails,
                                            @PathVariable(name = "userId") String userId,
                                            @PathVariable(name = "workflowId") Long workflowId,
                                            @PathVariable(name = "projectId") Long projectId,
                                            @PathVariable(name = "eventId") Long eventId) throws NotFoundException {
        Long deleted = eventService.deleteEvent(userId, workflowId, projectId, eventId);
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }

    @Operation(summary = "Pause an event", description = "Pause a specific event by its ID")
    @ApiResponse(responseCode = "200", description = "Event paused successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Event not found")
    @PatchMapping("/{eventId}/pause")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EventResponseDto> pauseEvent(@PathVariable Long eventId) throws NotFoundException {
        return ResponseEntity.ok(eventService.pauseEvent(eventId));
    }

    @Operation(summary = "Unpause an event", description = "Unpause a specific event by its ID")
    @ApiResponse(responseCode = "200", description = "Event unpaused successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Event not found")
    @PatchMapping("/{eventId}/unpause")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EventResponseDto> unpauseEvent(@PathVariable Long eventId) throws NotFoundException {
        return ResponseEntity.ok(eventService.unpauseEvent(eventId));
    }

    @Operation(summary = "Get events by date", description = "Retrieve a list of events by date")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponseDto.class)))
    @GetMapping("/date")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<EventResponseDto>> getEventsByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws NotFoundException {
        return ResponseEntity.ok(eventService.getEventsByDate(date));
    }

}
