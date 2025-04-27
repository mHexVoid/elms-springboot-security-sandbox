package com.hexvoid.nexusjpa.exceptionhandler;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Global exception handler to catch exceptions across all controllers.
 * Converts exceptions into well-structured JSON responses for the client.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles all BusinessExceptions in the application.
     * Builds a consistent error response including time, status, message, and request path.
     * 
     * @param ex      - The BusinessException thrown from any service/controller
     * @param request - Used to extract the request URI where exception occurred
     * @return ResponseEntity with status and ExceptionDetails body
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionDetails> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {

        ExceptionDetails details = new ExceptionDetails();

        // Setting the exact timestamp when exception occurred
        details.setTimestamp(LocalDateTime.now());

        // Numeric value of HTTP status (e.g., 404)
        details.setStatus(ex.getStatus().value());

        // Reason phrase for the status (e.g., "Not Found")
        details.setError(ex.getStatus().getReasonPhrase());

        // Actual message from the exception
        details.setMessage(ex.getMessage());

        // Path of the request that caused this error
        details.setPath(request.getRequestURI());

        // Returning structured error response to client
        return new ResponseEntity<>(details, ex.getStatus());
    }
}
