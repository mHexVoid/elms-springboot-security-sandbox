package com.hexvoid.employeeportal.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global Exception Handler for handling application-wide exceptions.
 * It catches exceptions thrown by the application and returns a structured response.
 */
@ControllerAdvice  // Enables centralized exception handling across all controllers.
public class ExceptionHandlerGlobal {

	/**
	 * Handles MyCustomExceptionClass and returns a formatted error response.
	 * 
	 * @param e The thrown custom exception.
	 * @return ResponseEntity containing ExceptionHandlerEntity with error details.
	 */
	@ExceptionHandler
	public ResponseEntity<ExceptionHandlerEntity> handleException(MyCustomExceptionClass e) {
		// Create a response entity with error details
		ExceptionHandlerEntity errorResponse = new ExceptionHandlerEntity();
		errorResponse.setMessage(e.getMessage());
		errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
		errorResponse.setTimeStamp(System.currentTimeMillis());

		// Return response entity with HTTP status 404 (Not Found)
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}
}