package com.hexvoid.nexusjpa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents the Appointment entity.
 * 
 * - Maps to the "appointment" table in the database.
 * - Each appointment is associated with one Doctor.
 * - A Doctor can have multiple appointments (One-to-Many).
 */
@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Name of the patient who booked the appointment.
     */
    @Column(name = "patient_name")
    private String patientName;

    /**
     * Date of the appointment.
     */
    @Column(name = "appointment_date")
    private String appointmentDate;

    /**
     * Many-to-One relationship with Doctor entity.
     * 
     * - The foreign key "doctor_id" in the "appointment" table links to the Doctor entity.
     * - Cascade operations (except REMOVE) allow updates and persistence operations to propagate.
     * - Deleting an appointment won't delete the doctor, as REMOVE is excluded.
     * - @JsonBackReference prevents infinite recursion in bidirectional JSON serialization.
     */
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
                          CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "doctor_id")
    @JsonBackReference
    private Doctor doctor;

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    /**
     * Setter for ID is intentionally omitted to preserve the integrity of the auto-generation strategy.
     * Manually setting the ID can interfere with Hibernate's identifier management,
     * potentially resulting in persistence exceptions or data integrity issues.
     */
    
//     public void setId(int id) {
//         this.id = id;
//     }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    // --- Debug-friendly toString() ---

    @Override
    public String toString() {
        return "Appointment [id=" + id + 
               ", patientName=" + patientName + 
               ", appointmentDate=" + appointmentDate + "]";
    }
}
