package ru.wwerlosh.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import ru.wwerlosh.controllers.dto.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ConnectionErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleConnectionRefusedError(ConnectionRefusedError error) {
        System.out.println(error.getMessage());
        return ResponseEntity.ok().body(new ErrorResponse("Invalid url"));
    }
}
