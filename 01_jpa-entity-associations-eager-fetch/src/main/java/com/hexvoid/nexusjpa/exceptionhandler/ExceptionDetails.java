package com.hexvoid.nexusjpa.exceptionhandler;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) for structuring API error response.
 * Helps in standardizing how errors are sent to the frontend/client.
 */
public class ExceptionDetails {

    // Time when the error occurred
    private LocalDateTime timestamp;

    // HTTP status code (e.g., 404, 500)
    private int status;

    // Reason phrase corresponding to the status (e.g., "Not Found", "Internal Server Error")
    private String error;

    // Detailed message from the exception
    private String message;

    // The URL path that caused the error
    private String path;

    // --- Getters and setters ---
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
