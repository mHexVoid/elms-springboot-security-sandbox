package com.hexvoid.employeeportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexvoid.employeeportal.entity.EmployeeAuthorities;
import com.hexvoid.employeeportal.entity.EmployeeCredentials;
import com.hexvoid.employeeportal.service.EmployeeSecretServiceImpl;

@RestController
@RequestMapping(value="/api")
public class CreateUser {

	private final EmployeeSecretServiceImpl employeeSecretService;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public CreateUser(EmployeeSecretServiceImpl employeeSecretService , PasswordEncoder passwordEncoder) {
		this.employeeSecretService = employeeSecretService;
		this.passwordEncoder = passwordEncoder;
	}

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
			return new ResponseEntity<>(HttpStatus.IM_USED);
		}

	}

	@GetMapping("/registered/user/findByEmail/{email}")
	ResponseEntity<EmployeeCredentials> findRegisteredUserbyEmail(@PathVariable String email){

		EmployeeCredentials byEmail = employeeSecretService.findByEmail(email);
		return ResponseEntity.ok(byEmail);
	}
}

