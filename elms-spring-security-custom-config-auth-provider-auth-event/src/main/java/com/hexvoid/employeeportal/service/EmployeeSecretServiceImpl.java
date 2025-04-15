package com.hexvoid.employeeportal.service;

import com.hexvoid.employeeportal.dao.EmployeeCredentialsDao;
import com.hexvoid.employeeportal.entity.EmployeeCredentials;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing employee credentials.
 * <p>
 * Implements EmployeeSecretsService and encapsulates business logic
 * for fetching and saving authentication-related employee data.
 */
@Service
public class EmployeeSecretServiceImpl implements EmployeeSecretsService {

    private final EmployeeCredentialsDao employeeCredentialsDao;

    /**
     * Constructor-based dependency injection of DAO layer.
     *
     * @param employeeCredentialsDao DAO for employee credentials
     */
    @Autowired
    public EmployeeSecretServiceImpl(EmployeeCredentialsDao employeeCredentialsDao) {
        this.employeeCredentialsDao = employeeCredentialsDao;
    }

    /**
     * Retrieves employee credentials based on the provided email.
     *
     * @param email the employee's email
     * @return EmployeeCredentials with associated authorities
     */
    @Override
    public EmployeeCredentials findByEmail(String email) {
        return employeeCredentialsDao.findByEmail(email);
    }

    /**
     * Persists employee credentials into the database.
     *
     * @param employeeCredentials the credentials to save
     * @return the saved EmployeeCredentials
     */
    @Override
    public EmployeeCredentials save(EmployeeCredentials employeeCredentials) {
        return employeeCredentialsDao.save(employeeCredentials);
    }
}
