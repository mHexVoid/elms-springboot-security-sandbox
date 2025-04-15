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
 * Custom Authentication Provider for validating user credentials manually.
 * This class will override Spring Security's default authentication mechanism.
 */
@Component
public class EmployeeUserNamePwdAuthenticationProvider implements AuthenticationProvider {

	// Injecting EmployeeUserDetailsService to fetch user details from the database
	private final EmployeeUserDetailsService userDetailsService;

	// Injecting PasswordEncoder to validate the encoded password
	private final PasswordEncoder passwordEncoder;

	// Constructor-based Dependency Injection
	EmployeeUserNamePwdAuthenticationProvider(EmployeeUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * This method authenticates the user by checking their username and password.
	 * If the credentials are correct, it returns an Authentication object.
	 * If the credentials are incorrect, it throws an exception.
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		// Extracting the username and password from authentication request
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		// Fetching user details from the database using EmployeeUserDetailsService
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		System.out.println("User: " + username + " is trying to log in.");
		System.out.println("DB Password: " + userDetails.getPassword());
		System.out.println("Entered Password: " + password);
		System.out.println("Password Match: " + passwordEncoder.matches(password, userDetails.getPassword()));

		// Checking if the entered password matches the stored encoded password
		if (passwordEncoder.matches(password, userDetails.getPassword())) {
			// If passwords match, authentication is successful
			return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
		} else {
			// If password is incorrect, throw an exception (Login fails)
			throw new BadCredentialsException("Bad credentials");
		}
	}

	/**
	 * This method tells Spring Security that this provider supports
	 * Username/Password based authentication.
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
