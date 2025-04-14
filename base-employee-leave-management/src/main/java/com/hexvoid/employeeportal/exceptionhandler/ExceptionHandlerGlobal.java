package com.hexvoid.employeeportal.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * <b>Global Exception Handler Class</b>
 * <p>
 * This class is responsible for handling exceptions globally throughout the application. It provides a centralized
 * mechanism for catching and managing exceptions, ensuring that all exceptions are processed in a consistent and
 * structured manner. It helps in generating structured error responses with relevant details such as the error message,
 * status code, and timestamp, making debugging and troubleshooting easier.
 * </p>
 * 
 * <b>Key Responsibilities:</b>
 * <ul>
 *   <li><b>Centralized Exception Handling:</b> The class handles all exceptions thrown in the application globally using Spring's @ControllerAdvice.</li>
 *   <li><b>Consistent Error Responses:</b> Ensures a standardized response structure for all exception types, enhancing maintainability and readability of error messages.</li>
 *   <li><b>Detailed Logging:</b> Error responses contain detailed information like the HTTP status code, message, and timestamp, aiding in efficient debugging and tracking of issues.</li>
 *   <li><b>Extensibility:</b> New exception handling logic can be easily added, making the handler flexible for different types of exceptions.</li>
 * </ul>
 * 
 * <b>Usage:</b>
 * This class is automatically invoked whenever an exception is thrown by any controller within the Spring Boot application.
 * It catches custom exceptions like {@link MyCustomExceptionClass} and returns the structured error response as per the configuration.
 */
@ControllerAdvice  // Indicates that this class will handle exceptions globally across all controllers in the application.
public class ExceptionHandlerGlobal {

    /**
     * <b>Handles MyCustomExceptionClass</b>
     * <p>
     * This method is invoked when an exception of type {@link MyCustomExceptionClass} is thrown. It creates a well-structured
     * error response that includes an HTTP status code, the exception message, and the current timestamp. This response is then
     * returned as part of the HTTP response to the client.
     * </p>
     * 
     * <b>Steps Performed:</b>
     * <ol>
     *   <li>Extracts the error message from the {@link MyCustomExceptionClass} exception.</li>
     *   <li>Sets the HTTP status code to 404 (Not Found), indicating that the requested resource could not be found.</li>
     *   <li>Captures the current timestamp to provide a record of when the error occurred.</li>
     *   <li>Returns the error response encapsulated in a {@link ResponseEntity} with the appropriate HTTP status code.</li>
     * </ol>
     * 
     * <b>Parameters:</b>
     * <ul>
     *   <li><i>e</i> - The {@link MyCustomExceptionClass} exception that was thrown.</li>
     * </ul>
     * 
     * <b>Returns:</b>
     * <ul>
     *   <li>A {@link ResponseEntity} containing an {@link ExceptionHandlerEntity} object that holds the error message,
     *       HTTP status code, and timestamp of the exception.</li>
     * </ul>
     * 
     * <b>Example:</b>
     * <pre>
     *   ResponseEntity<ExceptionHandlerEntity> response = handleException(new MyCustomExceptionClass("Resource not found"));
     * </pre>
     * 
     * @param e The {@link MyCustomExceptionClass} that was thrown.
     * @return A {@link ResponseEntity} containing the {@link ExceptionHandlerEntity} object with the structured error response.
     */
    @ExceptionHandler  // Handles the MyCustomExceptionClass thrown from any controller in the application
    public ResponseEntity<ExceptionHandlerEntity> handleException(MyCustomExceptionClass e) {
        // Create an error response object
        ExceptionHandlerEntity errorResponse = new ExceptionHandlerEntity();
        
        // Set the exception message in the error response
        errorResponse.setMessage(e.getMessage());
        
        // Set the HTTP status code for resource not found (404)
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        
        // Set the current timestamp of the error occurrence
        errorResponse.setTimeStamp(System.currentTimeMillis());

        // Return the response entity containing the error response with HTTP 404 status code
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
