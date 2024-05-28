package ru.kpfu.itis.lifeTrack.controller.rest.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import ru.kpfu.itis.lifeTrack.exception.NotFoundException;
import ru.kpfu.itis.lifeTrack.exception.security.TokenException;

import javax.security.auth.login.LoginException;

@ControllerAdvice
public class ExceptionHandlerController{

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleNotFound(HttpServletRequest req, Exception exception) {
        return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({JsonPatchException.class, JsonProcessingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleBadJsonRequest(HttpServletRequest req, Exception exception) {
        return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ AccessDeniedException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ TokenException.class, LoginException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handleAuthenticationException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
    }
}
