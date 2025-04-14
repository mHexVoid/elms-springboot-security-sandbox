package com.hexvoid.employeeportal.filter;

import com.hexvoid.employeeportal.constants.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * This filter is responsible for validating incoming JWT tokens from client requests.
 *
 * <p>
 * It is applied to all endpoints <strong>except</strong> <code>/user/details</code> (where the token is generated).
 * <br>
 * This filter:
 * <ul>
 *     <li>Extracts the token from the <code>Authorization</code> header</li>
 *     <li>Parses and validates the token using the secret key</li>
 *     <li>Extracts user details and sets them in Spring Security’s context</li>
 * </ul>
 * Once the context is set, Spring Security handles authorization based on roles/authorities.
 * </p>
 */
public class JWTTokenValidatorFilter extends OncePerRequestFilter {
	/**
	 * @param request
	 * @param response
	 * @param filterChain
	 * @throws ServletException
	 * @throws IOException
	 */


	/**
	 * Intercepts every request before controller execution to:
	 * <ul>
	 *     <li>Validate the JWT token and parse (if present in the request header.)</li>
	 *     <li>Extract user data and set authentication in SecurityContext</li>
	 * </ul>
	 *
	 * @param request     the incoming HTTP request from the client
	 * @param response    the outgoing HTTP response
	 * @param filterChain filter chain to pass request/response to the next filter
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an input/output error occurs
	 */

	@Override
	protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		// Step 1: Extract the JWT token from the Authorization header
		String jwt = request.getHeader(ApplicationConstants.JWT_HEADER_NAME);
		//if (jwt == null || jwt.isEmpty()) {
		//}
		if (jwt != null) {
			try {
				Environment environment = getEnvironment();
				if (environment != null) {

					// Step 2: Retrieve secret key from environment or fallback default
					String secretKeyEnv = environment.getProperty(
							ApplicationConstants.JWT_SECRET_KEY,
							ApplicationConstants.JWT_DEFAULT_SECRET_VALUE
							);

					SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyEnv.getBytes(StandardCharsets.UTF_8));

					if (secretKey != null) {

						// Step 3: Parse and validate the JWT token
						Claims claims = Jwts.parser()
								.verifyWith(secretKey)
								.build()
								.parseSignedClaims(jwt)
								.getPayload();

						// Step 4: Extract user information and authorities from the token
						String username = String.valueOf(claims.get("email"));
						String authorities = String.valueOf(claims.get("authorities"));

						//It will set authentication value as true under UsernamePasswordAuthenticationToken

						// Step 5: Set the user authentication in the SecurityContext for Spring Security
						Authentication authentication = new UsernamePasswordAuthenticationToken(
								username,
								null,
								AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
								);

						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				}
			} catch (Exception e) {
				// Token is invalid, expired, tampered or malformed
				throw new BadCredentialsException("Invalid JWT Token Received");
			}
		}
		// Continue with the remaining filter chain
		filterChain.doFilter(request, response);
	}

	/**
	 * This method decides whether this filter should be applied to the current request.
	 *
	 * <p>
	 * <strong>Logic:</strong> Skip this filter for the <code>/user/details</code> endpoint,
	 * since that’s where we generate the token — applying validation there would interfere.
	 * </p>
	 *
	 * <pre>
	 * Example:
	 * - Request to /user/details  → return true  → filter is skipped ❌
	 * - Request to /leave/apply   → return false → filter runs ✅
	 * </pre>
	 *
	 * @param request the current HTTP request
	 * @return true if the filter should NOT apply to this request
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		//Why not keeping ! here like in Token Generator
		return request.getServletPath().equals("/user/details");
	}
}