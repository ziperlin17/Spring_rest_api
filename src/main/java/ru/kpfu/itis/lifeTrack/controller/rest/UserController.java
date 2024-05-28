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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.lifeTrack.dto.response.UserDto;
import ru.kpfu.itis.lifeTrack.exception.user.UserAlreadyExistsException;
import ru.kpfu.itis.lifeTrack.exception.user.UserNotFoundException;
import ru.kpfu.itis.lifeTrack.model.user.UserEntity;
import ru.kpfu.itis.lifeTrack.security.jwt.JwtUserDetails;
import ru.kpfu.itis.lifeTrack.service.UserService;

@Tag(name = "User")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users", produces = { "application/json" })
@PreAuthorize("isAuthenticated()")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get a user by ID",
            description = "Retrieve a user's details using their ID.",
            operationId = "getUser",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User details retrieved successfully",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized request"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden request"
                    )
            }
    )
    @GetMapping(value = "{userId}")
    @PreAuthorize("#userDetails.id == #userId")
    public ResponseEntity<UserDto> getUser(@AuthenticationPrincipal JwtUserDetails userDetails,
                                           @PathVariable(name = "userId") String userId) throws UserNotFoundException {
        UserDto userDto = userService.getUser(userId);
        return ResponseEntity.ok(userDto);
    }

    @Operation(
            summary = "Create a new user",
            description = "Add a new user to the system.",
            operationId = "insertUser",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User created successfully",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized request"
                    )
            }
    )
    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<UserDto> insertUser(@AuthenticationPrincipal JwtUserDetails userDetails,
                                              @RequestBody UserEntity userEntity) throws UserAlreadyExistsException {
        UserDto userDto = userService.insertUser(userEntity);
        return ResponseEntity.ok(userDto);
    }

    @Operation(
            summary = "Patch a user",
            description = "Apply partial updates to a user using a JSON patch.",
            operationId = "patchUser",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User patched successfully",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized request"
                    )
            }
    )
    @RequestMapping(value = "{userId}", method = RequestMethod.PATCH, consumes = {"application/json"})
    @PreAuthorize("#userDetails.id == #userId")
    public ResponseEntity<UserDto> patchUser(@AuthenticationPrincipal JwtUserDetails userDetails,
                                             @PathVariable(name = "userId") String userId,
                                             @RequestBody JsonPatch jsonPatch) throws UserNotFoundException, JsonPatchException, JsonProcessingException {
        UserDto user = userService.patchUser(userId, jsonPatch);
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Update a user",
            description = "Update a user's details using the provided data.",
            operationId = "updateUser",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User updated successfully",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized request"
                    )
            }
    )
    @PutMapping(value = "{userId}", consumes = {"application/json"})
    @PreAuthorize("#userDetails.id == #userId")
    public ResponseEntity<UserDto> updateUser(@AuthenticationPrincipal JwtUserDetails userDetails,
                                              @PathVariable(name = "userId") String userId,
                                              @RequestBody UserEntity updated) throws UserNotFoundException {
        UserDto user = userService.updateUser(userId, updated);
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Delete a user",
            description = "Delete a user by their ID.",
            operationId = "deleteUser",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User deleted successfully",
                            content = {
                                    @Content(
                                            mediaType = "text/plain"
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized request"
                    )
            }
    )
    @DeleteMapping(value = "{userId}")
    @PreAuthorize("#userDetails.id == #userId")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal JwtUserDetails userDetails,
                                        @PathVariable(name = "userId") String userId) throws UserNotFoundException {
        return new ResponseEntity<>(userService.deleteUser(userId), HttpStatus.OK);
}

}
