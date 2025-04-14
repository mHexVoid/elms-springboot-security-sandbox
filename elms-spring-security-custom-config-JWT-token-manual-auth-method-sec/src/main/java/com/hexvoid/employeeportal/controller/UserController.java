package com.hexvoid.employeeportal.controller;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexvoid.employeeportal.constants.ApplicationConstants;
import com.hexvoid.employeeportal.entity.EmployeeAuthorities;
import com.hexvoid.employeeportal.entity.EmployeeCredentials;
import com.hexvoid.employeeportal.entity.LoginRequest;
import com.hexvoid.employeeportal.entity.LoginResponse;
import com.hexvoid.employeeportal.service.EmployeeSecretServiceImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * The {@code UserController} class exposes REST endpoints for handling user authentication, registration, and profile retrieval.
 * <p>
 * It is responsible for:
 * <ul>
 *   <li>User registration with password encryption and authority mapping.</li>
 *   <li>Authenticating users using JWT token strategy via {@link #customAuthentication(LoginRequest)}.</li>
 *   <li>Retrieving authenticated user details via {@link #getEmployeesDetailsAfterLogin(Authentication)}.</li>
 *   <li>Fetching user information by email and supporting admin-level queries.</li>
 * </ul>
 * </p>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>User registration with encrypted passwords and role assignments.</li>
 *   <li>Authentication of users via <code>/api/login</code> using {@link #customAuthentication(LoginRequest)} method.</li>
 *   <li>JWT token generation and secure transmission in response headers and body.</li>
 *   <li>Endpoints for fetching authenticated user details and querying users by email.</li>
 * </ul>
 *
 * <p><b>Security Context:</b></p>
 * <ul>
 *   <li>Spring Security manages access control using a custom authentication provider.</li>
 *   <li>All sensitive endpoints are secured via JWT tokens passed in headers.</li>
 * </ul>
 *
 * <p><b>JWT Token Handling:</b></p>
 * <ul>
 *   <li>The {@link #customAuthentication(LoginRequest)} method generates JWT tokens after successful login.</li>
 *   <li>JWT tokens include user-specific claims such as email and granted authorities.</li>
 *   <li>Tokens are signed using a secret key fetched from application properties to ensure integrity.</li>
 * </ul>
 *
 * <p><b>Flow of Authentication and Token Generation:</b></p>
 * <ol>
 *   <li>Users send login credentials to the <code>/api/login</code> endpoint.</li>
 *   <li>The credentials are validated using a custom authentication provider via {@link AuthenticationManager}.</li>
 *   <li>Upon success, a JWT token is generated and:
 *     <ul>
 *       <li>Returned in the response body.</li>
 *       <li>Also set in the HTTP response header under a custom header key.</li>
 *     </ul>
 *   </li>
 * </ol>
 */

@RestController
public class UserController {

	private final EmployeeSecretServiceImpl employeeSecretService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final Environment environment;

	/**
	 * Constructs a {@code UserController} with the specified services and components.
	 *
	 * @param employeeSecretService the service for employee-related operations
	 * @param passwordEncoder       the encoder for password encryption
	 * @param authenticationManager the authentication manager for login handling
	 * @param environment           the environment for fetching application properties
	 */

	@Autowired
	public UserController(EmployeeSecretServiceImpl employeeSecretService, PasswordEncoder passwordEncoder
			,AuthenticationManager authenticationManager ,Environment environment) {
		this.employeeSecretService = employeeSecretService;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager=authenticationManager;
		this.environment=environment;
	}

	/**
	 * <h2>Endpoint: GET /user/details</h2>
	 * <hr>
	 * <p>
	 * This endpoint returns the details of the authenticated user. Additionally, it generates a new JWT token 
	 * and sends it back to the client in the response header after successful authentication. The request 
	 * itself does not require a JWT token in the header, as the user is already authenticated, and the 
	 * authentication information is provided through Spring Security's SecurityContext.
	 * </p>
	 * <p><b>Key Details:</b></p>
	 * <ul>
	 *   <li><b>JWT Token Generation:</b> The JWT token is generated by the <code>JWTTokenGeneratorFilter</code> 
	 *       after the user is authenticated by Spring Security. The token includes claims like the user's 
	 *       email and authorities.</li>
	 *   <li><b>Authentication:</b> The <code>Authentication</code> object is automatically injected into the 
	 *       method by Spring Security. It contains the user's authentication details, such as the email (used 
	 *       as the username) and authorities.</li>
	 *   <li><b>Response Header:</b> The newly generated JWT token is returned in the response header as 
	 *       <code>Authorization: Bearer &lt;new_JWT&gt;</code>, and the client can store this token for 
	 *       future requests to protected endpoints.</li>
	 * </ul>
	 * 
	 * <p><b>Explanation of the Authentication parameter:</b></p>
	 * The <code>Authentication</code> parameter represents the current authenticated user. It is automatically 
	 * populated by Spring Security and contains:
	 * <ul>
	 *   <li><code>authentication.getName()</code> returns the email of the authenticated user.</li>
	 *   <li>The user's authorities (roles) can be accessed via <code>authentication.getAuthorities()</code>.</li>
	 * </ul>
	 * This information is used to fetch the user details (in this case, <code>EmployeeCredentials</code>) from 
	 * the database.
	 * 
	 * @param authentication The <code>Authentication</code> object provided by Spring Security, which contains 
	 *                       the current user's authentication details, including email and authorities.
	 * 
	 * @return The <code>EmployeeCredentials</code> object corresponding to the authenticated user, or 
	 *         <code>null</code> if no matching user is found.
	 * 
	 * @see org.springframework.security.core.Authentication
	 */
	@RequestMapping("/user/details")
	public EmployeeCredentials getEmployeesDetailsAfterLogin(Authentication authentication) {
		// Fetch user details using the email from the authentication object
		EmployeeCredentials user = employeeSecretService.findByEmail(authentication.getName());
		if (user == null) {
			return null;
		}
		return user;

	}

	/**
	 * <h2>Endpoint: POST /register/user</h2>
	 * <p>
	 * This endpoint registers a new user by encrypting their password and mapping their authorities. It ensures secure 
	 * password storage and a proper relationship between the {@link EmployeeCredentials} and {@link EmployeeAuthorities}.
	 * </p>
	 * <p><b>Password Encryption:</b> The password is encoded using the {@link PasswordEncoder} before saving it to the database.</p>
	 * <p><b>Authority Mapping:</b> The user's roles and authorities are properly mapped to establish a bidirectional relationship.</p>
	 * <p><b>Bidirectional Relationship:</b> A helper method inside the {@link EmployeeCredentials} class ensures both sides of the 
	 * relationship between {@link EmployeeCredentials} and {@link EmployeeAuthorities} are correctly set. This is important to 
	 * avoid foreign key constraint violations during persistence.</p>
	 * 
	 * @param employeeCredentials The {@link EmployeeCredentials} object containing the user's details, including 
	 *                            email, password, and roles.
	 * 
	 * @return A {@link ResponseEntity} containing the newly created {@link EmployeeCredentials} object with a 201 status 
	 *         if successful, or a 500 status if an error occurs during the registration process.
	 */
	@PostMapping("/register/user")
	ResponseEntity<EmployeeCredentials> createEmployeeCredentials(@RequestBody EmployeeCredentials employeeCredentials) {

		String pwd = passwordEncoder.encode(employeeCredentials.getPassword());
		employeeCredentials.setPassword(pwd);

		/*
		   Establishing a proper bidirectional relationship between 
		    EmployeeCredentials (Parent) and EmployeeAuthorities (Child) using the helper method inside EmployeeCredentials class.
		    ----------------------------------------------------------------------------------------------------------------------

		   Why is this important?
		 - In JPA/Hibernate, simply adding child entities to the parent’s collection is not enough.
		 - Each child must also explicitly reference its parent to maintain the foreign key relationship.
		 - Without this, Hibernate may leave the foreign key NULL or throw a constraint violation.

		   The helper method associateAuthorities() ensures both sides of the relationship are set correctly:
		 - Parent → Child: the child is added to the parent’s list.
		 - Child → Parent: the parent is set inside the child (foreign key gets mapped properly).

		   Alternatives:
		 - You can use a custom setter like setEmployeeAuthorities() to loop through and set back-references.
		 - For JSON serialization control (not DB mapping), use @JsonManagedReference and @JsonBackReference.

		   Step-by-step:
		 1️ Detach the current list of authorities temporarily.
		 2️ Re-establish the relationship using the helper method associateAuthorities().
		 3️ Save the parent entity — Hibernate will automatically persist the children due to CascadeType.ALL.
		 */

		//		List<EmployeeAuthorities> theEmployeeAuthorityList = employeeCredentials.getEmployeeAuthorities();
		//		employeeCredentials.setEmployeeAuthorities(null);
		//		for (EmployeeAuthorities currentEmployeeAuthority : theEmployeeAuthorityList) {
		//		    employeeCredentials.associateAuthorities(currentEmployeeAuthority);
		//		}

		EmployeeCredentials savedEmployeeCredentials = employeeSecretService.save(employeeCredentials);


		if (savedEmployeeCredentials != null) {
			return new ResponseEntity<>(savedEmployeeCredentials, HttpStatus.CREATED);

		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * <h2>Endpoint: GET /registered/user/findByEmail/{email}</h2>
	 * <p>
	 * This endpoint is used to retrieve a registered user's details by their email. It is typically used for administrative
	 * purposes or debugging. The request requires the JWT token in the header for authentication.
	 * </p>
	 * 
	 * @param email The email of the user whose details are to be retrieved.
	 * 
	 * @return A {@link ResponseEntity} containing the {@link EmployeeCredentials} object of the requested user, or 
	 *         a 404 status if the user does not exist.
	 */
	@GetMapping("/registered/user/findByEmail/{email}")
	ResponseEntity<EmployeeCredentials> findRegisteredUserbyEmail(@PathVariable String email) {

		EmployeeCredentials byEmail = employeeSecretService.findByEmail(email);
		return ResponseEntity.ok(byEmail);
	}


	/**
	 * Handles manual authentication for users using Spring Security and JWT.
	 *
	 * <p>This method accepts login credentials via a {@link LoginRequest} record,
	 * then manually initiates the authentication process by creating an unauthenticated
	 * {@link UsernamePasswordAuthenticationToken}. It delegates authentication to the
	 * {@code AuthenticationManager}, which uses a custom authentication provider.
	 *
	 * <p>If authentication is successful:
	 * <ul>
	 *   <li>Generates a JWT token with claims including email and authorities.</li>
	 *   <li>Signs the token using a secret key fetched from application properties.</li>
	 *   <li>Sets the JWT token in both the response body and HTTP headers.</li>
	 * </ul>
	 *
	 * <p>Record Classes:
	 * <ul>
	 *   <li>{@code LoginRequest} – A record holding login input (username, password).</li>
	 *   <li>{@code LoginResponse} – A record holding response status and generated JWT.</li>
	 * </ul>
	 *
	 * @param loginRequest the incoming login credentials
	 * @return {@code ResponseEntity<LoginResponse>} containing the JWT token in both header and body
	 */
	@PostMapping("/api/login")
	ResponseEntity<LoginResponse> customAuthentication(@RequestBody LoginRequest loginRequest) {
		String jwt = null;

		// Create unauthenticated authentication token using username and password
		Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(
				loginRequest.userName(), loginRequest.password());

		// Delegate authentication to custom provider via AuthenticationManager
		Authentication authenticationResponse = authenticationManager.authenticate(authentication);

		// On successful authentication, generate JWT token
		if (authenticationResponse != null && authenticationResponse.isAuthenticated()) {
			if (environment != null) {
				String secret = environment.getProperty(
						ApplicationConstants.JWT_SECRET_KEY,
						ApplicationConstants.JWT_DEFAULT_SECRET_VALUE);

				System.out.println("JWT Secret Key For Debug: " + secret);

				SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

				jwt = Jwts.builder()
						.issuer("Hex Void")
						.subject("JWT Token")
						.claim("email", authenticationResponse.getName())
						.claim("authorities", authenticationResponse.getAuthorities().stream()
								.map(grantedAuthority -> grantedAuthority.getAuthority())
								.collect(Collectors.joining(",")))
						.issuedAt(new Date())
						.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hrs
						.signWith(secretKey)
						.compact();
			}
		}

		// Set JWT token in HTTP header
		HttpHeaders headers = new HttpHeaders();
		headers.set(ApplicationConstants.JWT_HEADER_NAME, jwt);

		// Create response body with status and token
		LoginResponse responseBody = new LoginResponse(HttpStatus.OK.getReasonPhrase(), jwt);

		return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
	}

}
