package com.hexvoid.employeeportal.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * A simple filter that logs each request as it enters the Spring Security filter chain.
 * <p>
 * Useful for debugging and tracing incoming authentication attempts.
 */
public class AuthoritiesLoggingAtFilter implements Filter {
	/**
	 * @param request  The request to process
	 * @param response The response associated with the request
	 * @param chain    Provides access to the next filter in the chain for this filter to pass the request and response
	 * to for further processing
	 * @throws IOException
	 * @throws ServletException
	 */

	private static final Logger logger = LoggerFactory.getLogger(AuthoritiesLoggingAtFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		logger.info("Authentication Validation in AuthoritiesLoggingAtFilter");
		chain.doFilter(request, response);
	}


}
