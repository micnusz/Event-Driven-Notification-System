package com.micnusz.edns.error.exception;

import com.micnusz.edns.error.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse buildError(HttpStatus status, String message, String path) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailExists(EmailAlreadyExistsException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(buildError(status, exception.getMessage(), request.getRequestURI()));
    }
}
