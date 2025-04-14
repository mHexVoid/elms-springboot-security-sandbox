package com.hexvoid.employeeportal.exceptionhandler;

/**
 * Custom exception class for handling employee-related errors.
 * Extends RuntimeException, so it can be thrown at runtime.
 */
public class MyCustomExceptionClass extends RuntimeException {

	/**
	 * Constructor to create an exception with a custom message.
	 * @param message Error message to be displayed.
	 */
	public MyCustomExceptionClass(String message) {
		super(message);
	}
}
