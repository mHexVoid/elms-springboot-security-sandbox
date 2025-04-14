package com.hexvoid.employeeportal.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hexvoid.employeeportal.entity.EmployeeCredentials;
import com.hexvoid.employeeportal.service.EmployeeSecretServiceImpl;

/**
 * Custom implementation of UserDetailsService to fetch user details from the database.
 * 
 *  This Service **overrides** the default `JdbcUserDetailsManager` provided by Spring Security.
 *  It uses `EmployeeSecretServiceImpl` to fetch user details instead of executing SQL queries.
 *  It converts the retrieved user details into `UserDetails` for Spring Security authentication.
 */
@Service
public class EmployeeUserDetailsService implements UserDetailsService {

    private final EmployeeSecretServiceImpl employeeSecretService;

    /**
     * Constructor Injection of EmployeeSecretServiceImpl
     * 
     * @param employeeSecretService - Service class to interact with the database
     * 
     * 🔹 This ensures that the EmployeeSecretServiceImpl instance is automatically injected
     * by Spring at runtime.
     */
    @Autowired
    EmployeeUserDetailsService(EmployeeSecretServiceImpl employeeSecretService) {
        this.employeeSecretService = employeeSecretService;
    }

    /**
     * Loads user details from the database using the given username (email).
     * 
     * @param username - The email address of the user (used as the login identifier)
     * @return UserDetails object containing user information required for authentication
     * @throws UsernameNotFoundException - Thrown if no user is found with the given email.
     * 
     * 🔹 This method is called by Spring Security when a user tries to log in.
     * 🔹 It fetches user credentials from the database, converts roles to Spring Security format,
     *    and returns a `UserDetails` object for authentication.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //  Fetch the user details from the database using EmployeeSecretServiceImpl
        EmployeeCredentials employeeCredentials = employeeSecretService.findByEmail(username);
        
        //  If user is not found, throw an exception (Spring Security will handle it)
        if (employeeCredentials == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        //  Convert the role stored in the database into Spring Security's GrantedAuthority
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(employeeCredentials.getRoles()));

        //  Return a Spring Security `User` object with email, password, and roles
        return new User(employeeCredentials.getEmail(), employeeCredentials.getPassword(), authorities);
    }
}
