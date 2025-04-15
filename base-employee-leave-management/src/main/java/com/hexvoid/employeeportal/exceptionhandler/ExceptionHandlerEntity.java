package com.hexvoid.employeeportal.exceptionhandler;

/**
 * ExceptionHandlerEntity is a POJO (Plain Old Java Object) used to structure
 * error responses across the application whenever exceptions are handled globally.
 *
 * It ensures that the API returns consistent and meaningful error payloads,
 * especially when used with Spring's @ControllerAdvice for centralized exception handling.
 *
 * Fields:
 *  - status: HTTP status code (e.g., 404 for Not Found, 500 for Internal Server Error).
 *  - message: Human-readable error message describing the problem.
 *  - timeStamp: Epoch time (in milliseconds) when the exception occurred.
 *
 *  Useful For:
 *  - Standardizing error responses in REST APIs.
 *  - Improving client-side error parsing and logging.
 *  - Making the application production-grade and interview-ready.
 */
public class ExceptionHandlerEntity {

    private int status;  // HTTP Status Code (e.g., 404, 500)
    private String message;  // Detailed error message
    private long timeStamp;  // Timestamp when the exception occurred

    /**
     * Default no-argument constructor.
     * Required for serialization and deserialization (e.g., by Jackson).
     */
    public ExceptionHandlerEntity() {
    }

    // Getter & Setter Methods for status
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    // Getter & Setter Methods for message
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter & Setter Methods for timeStamp
    public long getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
