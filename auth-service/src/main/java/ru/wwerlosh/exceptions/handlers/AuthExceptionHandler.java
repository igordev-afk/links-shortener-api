package ru.wwerlosh.exceptions.handlers;

import jakarta.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.wwerlosh.controllers.dto.AuthorizationErrorResponse;
import ru.wwerlosh.exceptions.AuthorizationException;

@ControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<AuthorizationErrorResponse> signupExceptionHandler(AuthorizationException e,
                                                                             HttpServletRequest req) {
        AuthorizationErrorResponse error = new AuthorizationErrorResponse(
                ZonedDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                req.getRequestURI(),
                e.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
