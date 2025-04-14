package com.hexvoid.employeeportal.security;


import com.hexvoid.employeeportal.filter.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class SpringSecurityConfig {


	/**
	 * ❗ Note: If `EmployeeUserDetailsService` is present, this `JdbcUserDetailsManager` will be ignored.
	 *
	 *  This Bean provides JDBC-based authentication using the `employee_credentials` table.
	 *  It executes SQL queries to fetch user credentials and roles.
	 *  If `EmployeeUserDetailsService` is enabled, Spring will ignore this configuration.
	 */
	//	@Bean
	//	JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
	//		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
	//
	//		//  Query to fetch user credentials: Must return (username, password, enabled)
	//		jdbcUserDetailsManager.setUsersByUsernameQuery(
	//				"SELECT email, pwd, true FROM employee_credentials WHERE email = ?");
	//
	//		//  Query to fetch user roles: Must return (username, authority)
	//		jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
	//				"SELECT email, role FROM employee_credentials WHERE email = ?");
	//
	//		return jdbcUserDetailsManager;
	//	}


	/**
	 * Creates a {@link PasswordEncoder} bean using Spring Security's {@code DelegatingPasswordEncoder}.
	 * <p>
	 * This encoder supports multiple encoding formats and automatically chooses the appropriate one
	 * based on a prefix in the stored password (e.g., {bcrypt}).
	 *
	 * <p>It is a secure, flexible approach to password encoding and is the recommended strategy in Spring Security.
	 *
	 * @return a delegating password encoder supporting various encoding schemes
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}



	/**
	 * Defines a custom {@link AuthenticationManager} bean responsible for initiating the
	 * authentication process.
	 *
	 * <p>This implementation wires a custom {@link EmployeeUserNamePwdAuthenticationProvider}
	 * using the injected {@link EmployeeUserDetailsService} (custom implementation of {@link org.springframework.security.core.userdetails.UserDetailsService})
	 * and the {@link PasswordEncoder} bean.
	 *
	 * <p>The {@code ProviderManager} is the default Spring implementation of {@code AuthenticationManager}.
	 * Credentials are preserved after authentication by disabling {@code eraseCredentialsAfterAuthentication}.
	 *
	 * @param employeeUserDetailsService the custom user details service for loading user data
	 * @param passwordEncoder the encoder used to verify passwords securely
	 * @return a fully configured {@code AuthenticationManager} bean
	 */
	@Bean
	AuthenticationManager authenticationManager(EmployeeUserDetailsService employeeUserDetailsService,
			PasswordEncoder passwordEncoder)
	{
		EmployeeUserNamePwdAuthenticationProvider authProvider =
				new EmployeeUserNamePwdAuthenticationProvider(employeeUserDetailsService, passwordEncoder);

		ProviderManager providerManager = new ProviderManager(authProvider);
		//default value is true in spring implementation
		providerManager.setEraseCredentialsAfterAuthentication(false);

		return providerManager;
	}



	@Bean
	SecurityFilterChain chain(HttpSecurity http) throws Exception {


		http.authorizeHttpRequests(customizer ->

		customizer
		//This is for new section just below line
		.requestMatchers("/api/login").permitAll()
		.requestMatchers("/user/details").authenticated()
		.requestMatchers("/register/user").permitAll()
		.requestMatchers("/registered/user/findByEmail/**").permitAll()
		//Allowing in case internal error occurs control should not go spring security even if endpoint are not protected
		.requestMatchers("/error", "/error/**").permitAll()
		/*
		 * Ways of defining request Matcher
		 * Way 1
		 */
		//public end-points
		//by default it will match to all end points with POST, GET , PUT or DELETE Request
		.requestMatchers("/api/noauth/employees", "/api/noauth/employees/**").permitAll()
		//Way 2
		.requestMatchers(HttpMethod.POST, "/api/noauth/leaves").permitAll()
		.requestMatchers(HttpMethod.GET, "/api/noauth/leaves").permitAll()
		.requestMatchers(HttpMethod.GET, "/api/noauth/leaves/**").permitAll()
		.requestMatchers(HttpMethod.PUT, "/api/noauth/leaves/**").permitAll()
		.requestMatchers(HttpMethod.DELETE, "/api/noauth/leaves/**").permitAll()
		//Swagger UI & H2 Console
		.requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**").permitAll()
		//.requestMatchers(HttpMethod.GET,"/h2-console").permitAll()
		// .requestMatchers("/h2-console","/h2-console/**").permitAll()
		//Authenticated EndPoints
		.requestMatchers(HttpMethod.POST, "/api/employee").hasAuthority("UPDATE")
		.requestMatchers(HttpMethod.GET, "/api/employee").hasAnyAuthority("VIEW", "UPDATE")
		.requestMatchers(HttpMethod.GET, "/api/employee/**").hasAuthority("VIEW")
		.requestMatchers(HttpMethod.PUT, "/api/employee/**").hasAnyAuthority("UPDATE", "DELETE")
		.requestMatchers(HttpMethod.DELETE, "/api/employee/**").hasAuthority("DELETE")

		.requestMatchers(HttpMethod.POST, "/api/leave").hasAnyRole("ADMIN", "EMPLOYEE")
		.requestMatchers(HttpMethod.GET, "/api/leave").hasRole("ADMIN")
		.requestMatchers(HttpMethod.GET, "/api/leave/**").hasAnyRole("ADMIN", "EMPLOYEE")
		.requestMatchers(HttpMethod.PUT, "/api/leave/**").hasRole("ADMIN")
		.requestMatchers(HttpMethod.DELETE, "/api/leave/**").hasAnyRole("ADMIN", "EMPLOYEE")
				);

		//This will come to picture when dealing with UI
		//		CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
		//		http.csrf(csrfConfig -> csrfConfig
		//				.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
		//				.ignoringRequestMatchers("/error/**","/swagger-ui/**")
		//				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		//				);

		/**
		 * Add custom filters to the security filter chain.
		 * <p>
		 * Filters are added to enforce specific behaviors in the authentication and authorization process:
		 * </p>
		 * <ul>
		 *   <li><b>RequestValidationBeforeFilter:</b> Validates the incoming request before proceeding with authentication.</li>
		 *   <li><b>AuthoritiesLoggingAfterFilter:</b> Logs user authorities after authentication has occurred.</li>
		 *   <li><b>AuthoritiesLoggingAtFilter:</b> Logs user authorities at a different point in the filter chain.</li>
		 *   <li><b>JWTTokenGeneratorFilter:</b> Generates a new JWT token after successful authentication and includes it in the response header.</li>
		 *   <li><b>JWTTokenValidatorFilter:</b> Validates the JWT token sent by the client in the request header.</li>
		 * </ul>
		 */

		http.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class);
		http.addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class);
		http.addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class);
		http.addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class);
		http.addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class);
		//with STATELESS it will not be stored into session
		//If ALWAYS here it will generate JSESSION ID Always -- more explanation check --Security Context we need together when creating JSessionID
		http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


		http.csrf(csrf -> csrf.disable());

		http.formLogin(t -> t.disable());
		http.httpBasic(Customizer.withDefaults());
		//http.headers(t -> t.frameOptions(v ->v.sameOrigin()));
		return http.build();
	}

}
