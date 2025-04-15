package com.hexvoid.employeeportal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="employee_authorities")
public class EmployeeAuthorities {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name="authority")
	private String authority;

	@ManyToOne//(fetch = FetchType.EAGER)
	@JoinColumn(name="employee_id")
	@JsonIgnore
	private EmployeeCredentials employeeCredentials;


	@Override
	public String toString() {
		return "EmployeeAuthorities [id=" + id + ", authority=" + authority + "]";
	}


	//Getters and Setters

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}


	public EmployeeCredentials getEmployeeCredentials() {
		return employeeCredentials;
	}


	public void setEmployeeCredentials(EmployeeCredentials employeeCredentials) {
		this.employeeCredentials = employeeCredentials;
	}

}
