package com.hexvoid.employeeportal.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="employee_Credentials")
public class EmployeeCredentials {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="employee_id")
	private int id;

	@Column(name="employee_name")
	private String name;

	@Column(name="contact")
	private long contactDetails;

	@Column(name="email",unique = true)
	private String email;

	@Column(name="pwd")
	@JsonIgnore
	//or
	//@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@Column(name="role")
	private  String roles;

	@OneToMany(mappedBy = "employeeCredentials",
			cascade = CascadeType.ALL)
	private List<EmployeeAuthorities> employeeAuthorities;
	/*
	  Method: associateAuthorities(EmployeeAuthorities employeeAuthoritiestoAssociate)
	  Purpose:
	 - Ensures both sides of the bidirectional relationship (Parent: EmployeeCredentials, Child: EmployeeAuthorities) are properly linked.
	  Why is this necessary?
	 - In a bidirectional JPA association, it's not enough to add the child to the parent list.
	 - You must also set the parent inside the child, so Hibernate correctly maps the foreign key (`employee_id`).
	 - Without this, Hibernate may not persist the relationship properly, leading to NULL values or constraint violations.
	  Note:
	 - This method is currently not invoked, but referenced in the controller (CreateUser) as an educational approach to maintain data consistency.
	 */
	public void associateAuthorities(EmployeeAuthorities employeeAuthoritiestoAssociate) {
		if (employeeAuthorities == null) {
			employeeAuthorities = new ArrayList<>();
		}
		employeeAuthorities.add(employeeAuthoritiestoAssociate);
		// Critical: Set the parent in the child to maintain the back-reference.
		// Sets the back-reference so that Hibernate knows how to populate the join column (employee_id).
		employeeAuthoritiestoAssociate.setEmployeeCredentials(this);
	}

	/*
	  Alternate approach to maintain bidirectional mapping using the setter itself.
	  When using setEmployeeAuthorities(List<EmployeeAuthorities> ...):
	 - It’s advisable to loop through each authority and explicitly set this (EmployeeCredentials) as its parent.
	 - This ensures Hibernate has both directions of the relationship for persistence.
	  Use this if you prefer centralized relationship management via setters.
	 */
	public void setEmployeeAuthorities(List<EmployeeAuthorities> employeeAuthorities) {
		this.employeeAuthorities = employeeAuthorities;
		for (EmployeeAuthorities auth : employeeAuthorities) {
			auth.setEmployeeCredentials(this); 
		}
	}

	public List<EmployeeAuthorities> getEmployeeAuthorities() {
		return employeeAuthorities;
	}


	//To String 
	@Override
	public String toString() {
		return "EmployeeCredentials [name=" + name + ", contactDetails=" + contactDetails + ", email=" + email
				+ ", password=" + password + ", roles=" + roles + ", employeeAuthorities=" + employeeAuthorities + "]";
	}


	//Getters and Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public long getCotactDetails() {
		return contactDetails;
	}


	public void setCotactDetails(long contactDetails) {
		this.contactDetails = contactDetails;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getRoles() {
		return roles;
	}


	public void setRoles(String roles) {
		this.roles = roles;
	}



}
