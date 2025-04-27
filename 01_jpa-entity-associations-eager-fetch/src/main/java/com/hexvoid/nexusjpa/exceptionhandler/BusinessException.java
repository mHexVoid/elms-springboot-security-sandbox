package com.hexvoid.nexusjpa.exceptionhandler;

import org.springframework.http.HttpStatus;

/**
 * Custom exception class used for throwing business logic related exceptions.
 * Includes HTTP status to dynamically return meaningful error codes to clients.
 */
public class BusinessException extends RuntimeException {

    // This holds the HTTP status to return in the API response (e.g., 404, 400)
    private final HttpStatus status;

    /**
     * Constructor to initialize the exception with a custom message and status.
     * 
     * @param message - Custom message describing the error
     * @param status  - Corresponding HTTP status code (e.g. NOT_FOUND, BAD_REQUEST)
     */
    public BusinessException(String message, HttpStatus status) {
        super(message); // Pass message to parent Exception class
        this.status = status;
    }

    // Getter to retrieve the HTTP status code
    public HttpStatus getStatus() {
        return status;
    }
}
