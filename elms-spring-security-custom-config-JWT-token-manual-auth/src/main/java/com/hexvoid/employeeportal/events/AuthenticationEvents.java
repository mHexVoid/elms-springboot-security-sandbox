package com.hexvoid.employeeportal.events;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * Component to handle authentication-related events such as login success or failure.
 * <p>
 * This class listens to Spring Security's built-in authentication events and logs 
 * relevant information. Helps in auditing and debugging authentication flows.
 */
@Component
public class AuthenticationEvents {

	private static final Logger logger = Logger.getLogger(AuthenticationEvents.class.getName());

	/**
	 * Handles successful authentication events.
	 *
	 * @param successEvent event triggered when a user successfully logs in
	 */
	@EventListener
	public void onSuccess(AuthenticationSuccessEvent successEvent) {
		logger.log(Level.INFO, "Login successful for the user: {0}",
				new Object[]{successEvent.getAuthentication().getName()});
	}

	/**
	 * Handles failed authentication events.
	 *
	 * @param failureEvent event triggered when a user fails to authenticate
	 */
	@EventListener
	public void onFailure(AbstractAuthenticationFailureEvent failureEvent) {
		logger.log(Level.SEVERE,
				"Login failed for the user: {0} due to: {1}",
				new Object[]{
						failureEvent.getAuthentication().getName(),
						failureEvent.getException().getMessage()
		});
	}
}
