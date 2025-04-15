package com.hexvoid.employeeportal.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Custom Servlet Filter executed **before** Spring Security authentication.
 * <p>
 * It intercepts the `Authorization` header for Basic Auth, decodes it, and applies
 * custom validation logic (e.g., rejecting users with 'test' in their email).
 * <p>
 * If validation fails, the request is immediately rejected with a 400 Bad Request.
 */

public class RequestValidationBeforeFilter implements Filter {


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
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		//String uri = req.getRequestURI();
		String header = req.getHeader(HttpHeaders.AUTHORIZATION);
		if (header != null) {
			header = header.trim();
			if (StringUtils.startsWith(header, "Basic")) {
				byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
				byte[] decoded;
				try {
					decoded = Base64.getDecoder().decode(base64Token);
					String token = new String(decoded, StandardCharsets.UTF_8); //un:pw
					int delim = token.indexOf(':');
					if (delim == -1) {
						throw new BadCredentialsException("Inavalid Basic Authentication Token");

					}
					String email = token.substring(0, delim);
					// Reject users with 'test' in their email
					if (email.contains("test")) {
						res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}

				} catch (IllegalArgumentException e) {
					throw new BadCredentialsException("Invalid Basic Authentication Token");
				}
			}
		}
		//If we don't define all condition matches above then we will get 200Ok in response without any body
		//It's job of filter to define next chain 
		chain.doFilter(request, response);
	}
}

