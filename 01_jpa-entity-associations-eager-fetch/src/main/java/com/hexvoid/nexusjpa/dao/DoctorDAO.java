package com.hexvoid.nexusjpa.dao;

import com.hexvoid.nexusjpa.entity.Doctor;

/**
 * DAO Interface for performing operations on Doctor entities.
 * <p>
 * It abstracts the data access layer and allows various implementations (e.g., using JPA, JDBC).
 * 
 * This DAO focuses on persisting Doctor entities along with their associated Clinic and Appointments.
 */
public interface DoctorDAO {

    /**
     * Persists a Doctor entity along with their associated Clinic and Appointments.
     * <p>
     * Relationships are saved based on the cascade types defined in the Doctor entity:
     * <ul>
     *   <li><b>Clinic</b>: saved automatically via <code>CascadeType.ALL</code> since Doctor owns the relationship.</li>
     *   <li><b>Appointments</b>: saved via cascade operations (e.g., <code>CascadeType.PERSIST</code> or <code>MERGE</code>).</li>
     * </ul>
     *
     * @param doctor The Doctor entity to be saved.
     * @return The persisted Doctor entity with assigned ID and associated entities.
     */
    Doctor saveDoctor(Doctor doctor);

    /**
     * Retrieves a Doctor entity along with their associated Appointments using the Doctor ID.
     * <p>
     * Demonstrates eager fetching and the bidirectional relationship between Doctor and Appointment.
     *
     * @param id The ID of the Doctor to retrieve.
     * @return The Doctor entity including Appointments, or <code>null</code> if not found.
     */
    Doctor findDoctorWithAppointments(int id);
}
