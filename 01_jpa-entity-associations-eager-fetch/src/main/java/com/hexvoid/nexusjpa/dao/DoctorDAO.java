package com.hexvoid.nexusjpa.dao;

import com.hexvoid.nexusjpa.entity.Doctor;

/**
 * DAO Interface for performing operations on Doctor entities.
 * <p>
 * It abstracts the data access layer and allows various implementations (e.g., using JPA, JDBC).
 * 
 * This DAO focuses on saving Doctor entities along with their associated Clinic and Appointments.
 * The save operation handles the persistence of the Doctor entity and its relationships
 * based on the cascade types defined in the Doctor entity.
 */
public interface DoctorDAO {

    /**
     * Saves a Doctor entity along with their associated Clinic and Appointments.
     * <p>
     * The Doctor entity and its relationships (Clinic, Appointments) are persisted
     * based on the cascade configuration in the Doctor entity:
     * <ul>
     *   <li><b>Clinic</b>: Saved automatically via <code>CascadeType.ALL</code> 
     *   since the Doctor entity owns the relationship.</li>
     *   <li><b>Appointments</b>: Saved based on cascade operations defined in the entity 
     *   (e.g., <code>CascadeType.PERSIST</code> or <code>CascadeType.MERGE</code>).</li>
     * </ul>
     * <p>
     * The save operation will persist a new Doctor or update an existing one, depending on the entity state.
     *
     * @param doctor The Doctor entity to be saved.
     * @return The saved Doctor entity, including the assigned ID and associated entities.
     */
    Doctor saveDoctor(Doctor doctor);

    /**
     * Retrieves a Doctor entity along with their associated Appointments using the Doctor ID.
     * <p>
     * Demonstrates eager or lazy fetching and the bidirectional relationship between Doctor and Appointment.
     * If the Doctor is found, the associated Appointments are also retrieved.
     *
     * @param id The ID of the Doctor to retrieve.
     * @return The Doctor entity with their Appointments, or <code>null</code> if not found.
     */
    Doctor findDoctorWithAppointments(int id);
    
    /**
     * Deletes a Doctor entity using the provided Doctor ID.
     * <p>
     * This method removes the Doctor entity and all associated relationships based on the
     * cascading delete configuration in the Doctor entity.
     *
     * @param id The ID of the Doctor entity to delete.
     * @return A confirmation message indicating the successful deletion of the Doctor.
     */
    String deleteDoctor(int id);
   
}