package ru.kpfu.itis.lifeTrack.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.lifeTrack.dto.security.LoginDto;
import ru.kpfu.itis.lifeTrack.dto.security.SignupDto;
import ru.kpfu.itis.lifeTrack.dto.security.TokenDto;
import ru.kpfu.itis.lifeTrack.exception.security.TokenException;
import ru.kpfu.itis.lifeTrack.service.AuthenticationService;

import javax.security.auth.login.LoginException;

@RestController
@Slf4j
@RequestMapping(value = "/auth", produces = { "application/json" }, consumes = { "application/json" })
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            operationId = "login",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "api_key to be used in the secured-ping endpoint",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    implementation = TokenDto.class
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized request"
                    )
            }
    )
    @SecurityRequirements()
    @PostMapping("/login")
    @Transactional
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) throws LoginException {
        TokenDto token = authenticationService.login(loginDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }


    @Operation(
            operationId = "signup",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "api_key to be used in the secured-ping endpoint",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    implementation = TokenDto.class
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized request"
                    )
            }
    )
    @SecurityRequirements()
    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDto dto) throws LoginException {
        TokenDto token = authenticationService.signup(dto);
        return new ResponseEntity<>(token, HttpStatus.OK);

    }

    @Operation(
            operationId = "logout",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The end of the authenticated session and termination of the user's access to the website resources successfully"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized request"
                    )
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenDto dto) throws TokenException  {
        authenticationService.logout(dto);
        return new ResponseEntity<>(HttpEntity.EMPTY, HttpStatus.OK);
    }

    @Operation(
            operationId = "logout-all",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Termination of the authenticated session and termination of user access to website resources for all existing sessions successfully"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized request"
                    )
            }
    )
    @PostMapping("/logout-all")
    @Transactional
    public ResponseEntity<?> logoutAll(@RequestBody TokenDto dto) throws TokenException {
        authenticationService.logoutAll(dto);
        return new ResponseEntity<>(HttpEntity.EMPTY, HttpStatus.OK);
    }

    @Operation(
            operationId = "access-token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "api_key to be used in the secured-ping endpoint",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    implementation = TokenDto.class
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized request"
                    )
            }
    )
    @PostMapping("/access-token")
    public ResponseEntity<?> accessToken(@RequestBody TokenDto dto) throws TokenException {
        TokenDto token = authenticationService.accessToken(dto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @Operation(
            operationId = "access-token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "api_key to be used in the secured-ping endpoint",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(
                                                    implementation = TokenDto.class
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized request"
                    )
            }
    )
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenDto dto) throws TokenException  {
        try {
            TokenDto token = authenticationService.refreshToken(dto);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (TokenException ignored) {}
        return new ResponseEntity<>(HttpEntity.EMPTY, HttpStatus.UNAUTHORIZED);
    }
}
