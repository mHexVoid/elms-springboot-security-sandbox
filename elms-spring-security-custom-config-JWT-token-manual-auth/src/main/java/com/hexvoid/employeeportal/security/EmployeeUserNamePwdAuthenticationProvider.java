package com.hexvoid.employeeportal.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Custom {@link AuthenticationProvider} implementation for manual authentication
 * using a username and password.
 *
 * <p>This class overrides Spring Security's default authentication mechanism and
 * delegates user lookup to {@link EmployeeUserDetailsService}, validating the
 * provided credentials against the stored encoded password using a {@link PasswordEncoder}.
 *
 * <p>On successful authentication, a {@link UsernamePasswordAuthenticationToken}
 * is returned with the authenticated user's authorities.
 */
@Component
public class EmployeeUserNamePwdAuthenticationProvider implements AuthenticationProvider {

	private final EmployeeUserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;

	/**
	 * Constructs a new {@code EmployeeUserNamePwdAuthenticationProvider} with required dependencies.
	 *
	 * @param userDetailsService service to fetch user details from the database
	 * @param passwordEncoder encoder to validate raw and encoded password
	 */
	public EmployeeUserNamePwdAuthenticationProvider(EmployeeUserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Performs authentication by validating the provided username and password
	 * against the credentials stored in the database.
	 *
	 * @param authentication the authentication request object containing credentials
	 * @return a fully authenticated {@link UsernamePasswordAuthenticationToken} on success
	 * @throws AuthenticationException if authentication fails due to invalid credentials
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		System.out.println("User: " + username + " is trying to log in.");
		System.out.println("DB Password: " + userDetails.getPassword());
		System.out.println("Entered Password: " + password);
		System.out.println("Password Match: " + passwordEncoder.matches(password, userDetails.getPassword()));

		if (passwordEncoder.matches(password, userDetails.getPassword())) {
			return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
		} else {
			throw new BadCredentialsException("Bad credentials");
		}
	}

	/**
	 * Indicates whether this {@code AuthenticationProvider} supports the specified
	 * authentication type.
	 *
	 * @param authentication the authentication class
	 * @return {@code true} if {@code UsernamePasswordAuthenticationToken} is supported
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
