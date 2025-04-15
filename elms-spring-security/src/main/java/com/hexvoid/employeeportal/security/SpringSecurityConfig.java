package com.hexvoid.employeeportal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security Configuration class.
 *
 * Defines authentication mechanism (via in-memory users) and authorizes access
 * to REST endpoints using role-based access control.
 */
@Configuration
public class SpringSecurityConfig {

    /**
     * Registers in-memory users with case-sensitive username matching using a custom
     * {@link CaseSensitiveUserDetailsManager}.
     *
     * <p>
     * Roles and authorities:
     * <ul>
     *     <li>EMPLOYEE → Role: EMPLOYEE</li>
     *     <li>ADMIN → Roles: ADMIN, EMPLOYEE</li>
     * </ul>
     * </p>
     *
     * @return a case-sensitive in-memory user manager
     */
    @Bean
    UserDetailsService userDetailsService() {
        UserDetails employee = User.withUsername("EMPLOYEE")
                .password("{noop}1234")
                .roles("EMPLOYEE")
                .build();

        UserDetails adminEmployee = User.withUsername("ADMIN")
                .password("{noop}1234")
                .authorities("ROLE_ADMIN", "ROLE_EMPLOYEE")
                .build();

        return new CaseSensitiveUserDetailsManager(employee, adminEmployee);
    }

    /**
     * Alternative default Spring Security user manager (case-insensitive).
     * Commented out in favor of custom implementation.
     */
    // @Bean
    // UserDetailsService useDetailService() {
    //     UserDetails employee = User.withUsername("EMPLOYEE")
    //             .password("{noop}1234")
    //             .roles("EMPLOYEE")
    //             .build();
    //
    //     UserDetails adminEmployee = User.withUsername("ADMIN")
    //             .password("{noop}1234")
    //             .authorities("ROLE_EMPLOYEE", "ROLE_ADMIN")
    //             .build();
    //
    //     return new InMemoryUserDetailsManager(employee, adminEmployee);
    // }

    /**
     * Configures security filters for HTTP requests.
     *
     * <p>Features:</p>
     * <ul>
     *     <li>Case-sensitive user authentication via {@code Basic Auth}</li>
     *     <li>Role-based access to endpoints</li>
     *     <li>Public access to Swagger, H2 Console, and open API paths</li>
     *     <li>CSRF and FormLogin disabled for stateless REST APIs</li>
     * </ul>
     *
     * @param http HttpSecurity instance
     * @return SecurityFilterChain
     * @throws Exception if any configuration fails
     */
    @Bean
    SecurityFilterChain chain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(customizer ->
            customizer
                // Allow all error endpoints to bypass security filters
                .requestMatchers("/error", "/error/**").permitAll()

                // --- Public Endpoints ---
                // Method 1: All HTTP methods
                .requestMatchers("/api/noauth/employees", "/api/noauth/employees/**").permitAll()

                // Method 2: Specific HTTP methods
                .requestMatchers(HttpMethod.POST, "/api/noauth/leaves").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/noauth/leaves").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/noauth/leaves/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/noauth/leaves/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/noauth/leaves/**").permitAll()

                // Swagger & H2 Console
                .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/h2-console", "/h2-console/**").permitAll()

                // --- Protected Endpoints ---
                // Employee APIs
                .requestMatchers(HttpMethod.POST, "/api/employee").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/employee").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/employee/**").hasAnyRole("ADMIN", "EMPLOYEE")
                .requestMatchers(HttpMethod.PUT, "/api/employee/**").hasAnyRole("ADMIN", "EMPLOYEE")
                .requestMatchers(HttpMethod.DELETE, "/api/employee/**").hasRole("ADMIN")

                // Leave APIs
                .requestMatchers(HttpMethod.POST, "/api/leave").hasAnyRole("ADMIN", "EMPLOYEE")
                .requestMatchers(HttpMethod.GET, "/api/leave").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/leave/**").hasAnyRole("ADMIN", "EMPLOYEE")
                .requestMatchers(HttpMethod.PUT, "/api/leave/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/leave/**").hasAnyRole("ADMIN", "EMPLOYEE")
        );

        // --- Additional Security Config ---
        http.csrf(csrf -> csrf.disable());                     // Disable CSRF for REST API
        http.formLogin(login -> login.disable());              // Disable form login (stateless API)
        http.httpBasic(Customizer.withDefaults());             // Enable HTTP Basic Authentication
        http.headers(headers -> headers.frameOptions(opt -> opt.sameOrigin())); // Allow H2 console

        return http.build();
    }
}
