package com.hexvoid.employeeportal.filter;

import com.hexvoid.employeeportal.constants.ApplicationConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * This filter is responsible for generating a JWT (JSON Web Token)
 * and sending it back to the client in the response header after successful authentication.
 *
 * <p>
 * This filter executes only for the `/user/details` endpoint
 * and should be positioned in the Spring Security filter chain
 * after the authentication has been successfully performed.
 * </p>
 */
public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

	/**
	 * @param request
	 * @param response
	 * @param filterChain
	 * @throws ServletException
	 * @throws IOException
	 */

	/**
	 * Executes once per request and is triggered only for specific paths (defined in shouldNotFilter).
	 * If the user is already authenticated (via Spring Security), this filter will:
	 * <ol>
	 *     <li>Extract the current authenticated principal</li>
	 *     <li>Build a secure JWT using claims like email and authorities</li>
	 *     <li>Sign the token using a secret key from environment or default fallback</li>
	 *     <li>Attach the token to the response header with key: <b>Authorization</b></li>
	 * </ol>
	 *
	 * @param request     the HTTP request
	 * @param response    the HTTP response
	 * @param filterChain the rest of the filter chain
	 * @throws ServletException in case of servlet errors
	 * @throws IOException      in case of IO errors
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		// Step 1: Retrieve the current authenticated user's details from SecurityContext
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		// Step 2: Proceed only if the user is authenticated
		if (authentication != null) {
			Environment environment = getEnvironment();
			if (environment != null) {

				// Step 3: Fetch the JWT secret key from env, or fall back to a default secure value
				//
				// Why use a fallback?
				// --------------------
				// In local/dev environments, environment variables may not be set.
				// Using a fallback value ensures the app doesn’t crash and can still generate tokens.
				//
				// But in production, it is highly recommended to always define your own secure key via env variables
				// like Docker secrets, .env files, or Kubernetes config maps.
				//
				//  Example production-level secure secret (recommended 256-bit key for HMAC-SHA256):
				// jwt.secretKey=a8VYmKz7nL5xQ2pNfJrGcTeWvBdYzXh9UsMwEnRbTqOjSl3K

				//System.out.println("SECRET: " + environment.getProperty("jwt.secretKey"));
				//System.out.println("DEFAULT SECRET: " + environment.getProperty("jwt.defaultSecretKey"));

				//Get Property is having 2 parameter in case if no secret key is there it will fall to default value
				String secret = environment.getProperty(
						ApplicationConstants.JWT_SECRET_KEY,
						ApplicationConstants.JWT_DEFAULT_SECRET_VALUE
						);


				System.out.println("JWT Secret Key For Debug: " + secret); // Avoid printing it's just for learning reference
				SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

				// Step 4: Build the JWT token with custom claims
				String jwt = Jwts.builder()
						.issuer("Hex Void")   // Identifies the token creator
						.subject("JWT Token") // General description of the token
						.claim("email", authentication.getName()) // Add user's email (unique identity)
						.claim("authorities",authentication.getAuthorities().stream()
								.map(grantedAuthority ->
								grantedAuthority.getAuthority().toString())
								.collect(Collectors.joining(","))) // Convert authorities to CSV
						.issuedAt(new Date()) // Token issue time
						.expiration(new Date(new Date().getTime() + 1000 * 60 * 60 * 24)) // Token validity: 24 hours
						.signWith(secretKey) // Sign using HMAC SHA algorithm and the secret key
						.compact();
				// Step 5: Set the generated JWT token in response header to be used by client
				response.setHeader(ApplicationConstants.JWT_HEADER_NAME, jwt);
			}
		}
		// Continue with the remaining filter chain
		filterChain.doFilter(request, response);
	}


	/**
	 * This method determines whether this filter should be skipped for the current request.
	 *
	 * <p><strong>Logic:</strong></p>
	 * The filter will <strong>only apply</strong> to the endpoint <code>/user/details</code>.
	 * For all other paths, this filter will be skipped.
	 *
	 * <pre>
	 * How it works:
	 * - request.getServletPath() retrieves the current request path.
	 * - .equals("/user/details") checks if it matches our intended endpoint.
	 * - ! (logical NOT) inverts the result.
	 *
	 *     If the current request path is "/user/details":
	 *         → equals(...) = true
	 *         → !true = false → Filter is NOT skipped (it will run )
	 *
	 *     If the current request path is something else:
	 *         → equals(...) = false
	 *         → !false = true → Filter is skipped
	 * </pre>
	 *
	 * <p><strong>Why use this:</strong></p>
	 * To ensure that JWT token generation logic only executes for a specific endpoint
	 * (in this case <code>/user/details</code>), preventing unnecessary overhead or
	 * duplicate tokens being generated on every request.
	 *
	 * <p><strong>Best Practice Tip:</strong></p>
	 * Filters like this should be restricted to relevant endpoints only, both for performance
	 * and security purposes. You can also use patterns (e.g. using {@code AntPathMatcher})
	 * for more flexible matching like <code>/api/**</code>.
	 *
	 * @param request the current HTTP request
	 * @return true if the filter should be skipped for this request, false if it should be applied
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

		//return false; -- Do not run this filter for any request where the URL path is not /user/details
		return !request.getServletPath().equals("/user/details");
	}
}
