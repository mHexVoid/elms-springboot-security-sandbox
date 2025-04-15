package com.hexvoid.employeeportal.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hexvoid.employeeportal.entity.EmployeeAuthorities;
import com.hexvoid.employeeportal.entity.EmployeeCredentials;
import com.hexvoid.employeeportal.service.EmployeeSecretServiceImpl;

/**
 * Custom implementation of {@link UserDetailsService} to provide authentication
 * details for Spring Security.
 *
 * <p>This service replaces the default {@code JdbcUserDetailsManager} by programmatically
 * retrieving user information from the database via {@link EmployeeSecretServiceImpl}.
 * It transforms {@link EmployeeCredentials} and associated {@link EmployeeAuthorities}
 * into Spring Security's {@link UserDetails} and {@link GrantedAuthority} objects.
 */
@Service
public class EmployeeUserDetailsService implements UserDetailsService {

	private final EmployeeSecretServiceImpl employeeSecretService;

	/**
	 * Constructs the {@code EmployeeUserDetailsService} with a dependency on
	 * {@link EmployeeSecretServiceImpl}.
	 *
	 * @param employeeSecretService the service responsible for fetching user credentials
	 */
	@Autowired
	public EmployeeUserDetailsService(EmployeeSecretServiceImpl employeeSecretService) {
		this.employeeSecretService = employeeSecretService;
	}

	/**
	 * Loads user-specific data by username (used here as email) during authentication.
	 *
	 * <p>This method is automatically invoked by Spring Security to retrieve user details.
	 * It converts user roles into Spring Security compatible {@code GrantedAuthority}
	 * and returns a {@code UserDetails} object for further processing.
	 *
	 * @param username the email identifier of the user trying to authenticate
	 * @return a fully populated {@link UserDetails} object
	 * @throws UsernameNotFoundException if no matching user is found
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		EmployeeCredentials employeeCredentials = employeeSecretService.findByEmail(username);

		if (employeeCredentials == null) {
			throw new UsernameNotFoundException("User not found with email: " + username);
		}


		// Convert roles from the database into Spring Security's GrantedAuthority format.
		// --- Learning Reference ---
		// Option 1: If roles are stored as a single string (e.g., "ROLE_EMPLOYEE"), you can convert them like this:
		// List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(employeeCredentials.getRoles()));

		// Option 2: If roles are stored in a separate collection (e.g., List<EmployeeAuthorities>), you can loop through them:
		// List<EmployeeAuthorities> employeeAuthorities = employeeCredentials.getEmployeeAuthorities();
		// List<GrantedAuthority> authorities = new ArrayList<>();
		// for (EmployeeAuthorities authority : employeeAuthorities) {
		//          authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
		// }

		// Note: The above approaches are for learning/demo purposes.
		// In production, prefer streaming the collection directly as shown in the final implementation.


		List<SimpleGrantedAuthority> authorities = employeeCredentials
				.getEmployeeAuthorities()
				.stream()
				.map(auth -> new SimpleGrantedAuthority(auth.getAuthority()))
				.collect(Collectors.toList());

		return new User(employeeCredentials.getEmail(), employeeCredentials.getPassword(), authorities);
	}
}
