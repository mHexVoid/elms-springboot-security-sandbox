package com.hexvoid.employeeportal.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Custom filter that executes **after** Spring Security authentication is complete.
 * <p>
 * It retrieves the authenticated user from the SecurityContext and logs their 
 * granted authorities (roles/permissions). Useful for post-auth auditing.
 */
public class AuthoritiesLoggingAfterFilter implements Filter {

	private static final Logger logger =LoggerFactory.getLogger(AuthoritiesLoggingAfterFilter.class);


	/**
	 * @param request  The request to process
	 * @param response The response associated with the request
	 * @param chain    Provides access to the next filter in the chain for this filter to pass the request and response
	 *                 to for further processing
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			logger.info("User "+authentication.getName()+" successfully Authenticated"
					+" has the Authorities of type "+authentication.getAuthorities().toString());
		}
		chain.doFilter(request, response);
	}
}
