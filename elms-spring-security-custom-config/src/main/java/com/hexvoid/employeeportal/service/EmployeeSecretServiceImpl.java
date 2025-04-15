package com.hexvoid.employeeportal.service;

import com.hexvoid.employeeportal.dao.EmployeeCredentialsDao;
import com.hexvoid.employeeportal.entity.EmployeeCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service implementation for handling secure employee credentials operations.
 *
 * <p>This service interacts with the DAO layer to perform CRUD operations on employee credentials.</p>
 */
@Service
public class EmployeeSecretServiceImpl implements EmployeeSecretsService {

    private final EmployeeCredentialsDao employeeCredentialsDao;

    /**
     * Constructor-based dependency injection of the DAO.
     *
     * @param employeeCredentialsDao the data access object for credentials
     */
    @Autowired
    public EmployeeSecretServiceImpl(EmployeeCredentialsDao employeeCredentialsDao) {
        this.employeeCredentialsDao = employeeCredentialsDao;
    }

    /**
     * Finds an employee credential record by email.
     *
     * @param email the email to search for
     * @return the employee credentials, or null if not found
     */
    @Override
    public EmployeeCredentials findByEmail(String email) {
        return employeeCredentialsDao.findByEmail(email);
    }

    /**
     * Persists the given employee credentials in the data store.
     *
     * @param employeeCredentials the credentials to save
     * @return the saved credentials object
     */
    @Override
    public EmployeeCredentials save(EmployeeCredentials employeeCredentials) {
        return employeeCredentialsDao.save(employeeCredentials);
    }
}
