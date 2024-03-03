package ru.wwerlosh.exceptions.handlers;

import jakarta.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.wwerlosh.controllers.dto.InvalidTokenErrorResponse;
import ru.wwerlosh.exceptions.InvalidTokenException;

@ControllerAdvice
public class InvalidTokenExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<InvalidTokenErrorResponse> invalidTokenExceptionHandler(InvalidTokenException e,
                                                                                  HttpServletRequest req) {
        InvalidTokenErrorResponse error = new InvalidTokenErrorResponse(
                ZonedDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                req.getRequestURI(),
                e.getMessage(),
                e.getJwt()
        );

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}
