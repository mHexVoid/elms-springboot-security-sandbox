package com.hexvoid.employeeportal.exceptionhandler;

/**
 * This class represents a structured response for handling exceptions.
 * It ensures that every exception response has a status code, a message, and a timestamp.
 */
public class ExceptionHandlerEntity {

    private int status;  // HTTP Status Code (e.g., 404, 500)
    private String message;  // Detailed error message
    private long timeStamp;  // Timestamp when the exception occurred

    //Default Constructor
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
