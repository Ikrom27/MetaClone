package ru.metaclone.security.handler;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.metaclone.security.exceptions.BaseSecurityException;
import ru.metaclone.security.response.ErrorResponse;

import java.time.Instant;

@RestControllerAdvice
public class SecureExceptionHandler {
    @ExceptionHandler(BaseSecurityException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseSecurityException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now(),
                ex.getStatus().value(),
                ex.getCode(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }
}