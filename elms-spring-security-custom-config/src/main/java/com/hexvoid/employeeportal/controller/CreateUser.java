package com.hexvoid.employeeportal.controller;

import com.hexvoid.employeeportal.entity.EmployeeCredentials;
import com.hexvoid.employeeportal.service.EmployeeSecretServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller to handle user registration operations.
 * 
 * <p>This controller provides an endpoint to create new employee user credentials.
 * Passwords are encoded before persisting for security reasons.</p>
 */
@RestController
@RequestMapping(value = "/api")
public class CreateUser {

    private final EmployeeSecretServiceImpl employeeSecretService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor-based dependency injection for service and password encoder.
     *
     * @param employeeSecretService the service handling user persistence
     * @param passwordEncoder encoder used to hash raw passwords
     */
    @Autowired
    public CreateUser(EmployeeSecretServiceImpl employeeSecretService, PasswordEncoder passwordEncoder) {
        this.employeeSecretService = employeeSecretService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Endpoint to register a new user.
     * 
     * <p>The password from the request body is encoded before saving to ensure security.
     * If saved successfully, a 201 CREATED response is returned with the saved entity.
     * Otherwise, an HTTP 226 (IM_USED) is returned indicating the request failed.</p>
     *
     * @param employeeCredentials the employee credentials payload
     * @return ResponseEntity with saved credentials or error status
     */
    @PostMapping("/register/user")
    public ResponseEntity<EmployeeCredentials> createEmployeeCredentials(@RequestBody EmployeeCredentials employeeCredentials) {
        String encodedPassword = passwordEncoder.encode(employeeCredentials.getPassword());
        employeeCredentials.setPassword(encodedPassword);

        EmployeeCredentials savedEmployeeCredentials = employeeSecretService.save(employeeCredentials);

        if (savedEmployeeCredentials != null) {
            return new ResponseEntity<>(savedEmployeeCredentials, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.IM_USED); // Could be refined with custom error
        }
    }
}
