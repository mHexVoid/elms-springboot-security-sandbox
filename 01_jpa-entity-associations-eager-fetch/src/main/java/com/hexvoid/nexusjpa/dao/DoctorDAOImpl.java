package com.hexvoid.nexusjpa.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hexvoid.nexusjpa.entity.Doctor;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

/**
 * DAO Implementation for Doctor-related database operations.
 *
 * Responsible for persisting Doctor entities along with their associated
 * Clinic and Appointment entities using JPA's EntityManager.
 */
@Repository // Registers this implementation as a Spring-managed bean
public class DoctorDAOImpl implements DoctorDAO {

    // Injected EntityManager for performing persistence operations
    private final EntityManager entityManager;

    /**
     * Constructor-based dependency injection of EntityManager.
     *
     * @param entityManager JPA EntityManager provided by Spring
     */
    @Autowired
    public DoctorDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Persists a Doctor entity along with its associated Clinic and Appointments.
     *
     * Includes:
     * - Clinic (via CascadeType.ALL – relationship owned by Doctor)
     * - Appointments (typically via CascadeType.PERSIST / MERGE, depending on configuration)
     *
     * @Transactional ensures:
     * - All persistence operations are executed as a single atomic transaction
     * - If any part fails, the transaction is rolled back to maintain data integrity
     *
     * @param doctor The Doctor entity to be saved (with nested Clinic and Appointments)
     * @return The persisted Doctor entity
     */
    @Override
    @Transactional
    public Doctor saveDoctor(Doctor doctor) {
        return entityManager.merge(doctor);
    }

    /**
     * Retrieves a Doctor entity by ID, including their associated Appointments.
     *
     * Note:
     * - Appointments are fetched eagerly or lazily based on mapping configuration in the entity.
     *
     * @param id The primary key of the Doctor to retrieve
     * @return The Doctor entity with Appointments, or null if not found
     */
    @Override
    public Doctor findDoctorWithAppointments(int id) {
        return entityManager.find(Doctor.class, id);
    }
}
