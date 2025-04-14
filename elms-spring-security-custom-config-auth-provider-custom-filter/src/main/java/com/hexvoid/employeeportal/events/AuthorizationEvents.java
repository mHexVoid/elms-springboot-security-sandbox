package com.hexvoid.employeeportal.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

/**
 * Component for handling authorization events like access denied.
 * <p>
 * Listens to Spring Security's authorization failure events and logs detailed
 * information including username and decision, aiding in access control auditing.
 */
@Component
public class AuthorizationEvents {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationEvents.class);

    /**
     * Handles authorization denial events.
     * <p>
     * Triggered when an authenticated user attempts to access a secured resource 
     * without the necessary permissions.
     *
     * @param deniedEvent the event indicating the authorization was denied
     */
    @EventListener
    public void onFailure(AuthorizationDeniedEvent deniedEvent) {
        logger.error("Authorization failed for the user: {} due to: {}",
                deniedEvent.getAuthentication().get().getName(),
                deniedEvent.getAuthorizationDecision().toString());
    }
}
