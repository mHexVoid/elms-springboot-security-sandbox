package com.hexvoid.employeeportal.exceptionhandler;

/**
 * <b>MyCustomExceptionClass</b> is a custom exception class that extends {@link RuntimeException}.
 * This exception is designed to handle employee-related errors within the application.
 * 
 * <p>This exception is unchecked, meaning it extends {@link RuntimeException}, allowing it to be 
 * thrown at runtime without the need to explicitly declare it in method signatures.</p>
 * 
 * <b>Usage:</b> This custom exception should be used when an employee-related error occurs, such as 
 * invalid employee data, non-existent employees, or any other business logic violations specific 
 * to the employee portal.
 * 
 * <p>It provides a clear and specific error message to help troubleshoot and resolve issues efficiently.</p>
 */
public class MyCustomExceptionClass extends RuntimeException {

    /**
     * Constructs a new {@code MyCustomExceptionClass} with the specified detail message.
     * The error message provides specific information about the exception that occurred.
     * 
     * <p>This constructor passes the provided message to the superclass {@link RuntimeException} 
     * to store the error message, which can later be retrieved using the {@link #getMessage()} method.</p>
     * 
     * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public MyCustomExceptionClass(String message) {
        super(message);
    }
}
