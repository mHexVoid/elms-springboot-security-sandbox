package com.hexvoid.employeeportal.service;

import java.util.List;

import com.hexvoid.employeeportal.entity.Employees;

/**
 * Service layer interface extending DAO for Employee operations.
 * Helps in decoupling DAO from controllers and ensuring separation of concerns.
 */
public interface EmployeeService  {
	
	Employees save(Employees theEmployee);
	List<Employees> findAll();
	Employees findById(Long id);
	void deleteById(Long id) ;
	
}
