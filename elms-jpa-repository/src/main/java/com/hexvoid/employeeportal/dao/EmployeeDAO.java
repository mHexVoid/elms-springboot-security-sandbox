package com.hexvoid.employeeportal.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexvoid.employeeportal.entity.Employees;

/**
 * EmployeeDAO interface defines the CRUD operations for Employee entities.
 * It acts as a contract that the implementation class (EmployeeDaoImpl) must fulfill.
 */

public interface EmployeeDAO extends JpaRepository<Employees, Long>{
    
   
}
