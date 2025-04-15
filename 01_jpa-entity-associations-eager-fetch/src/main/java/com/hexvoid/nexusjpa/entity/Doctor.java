package com.hexvoid.nexusjpa.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Represents a Doctor who may work in a Clinic and manage Appointments.
 * 
 * This entity maps to the "doctor" table in the database.
 * 
 * - Has a One-to-One relationship with Clinic.
 * - Has a One-to-Many relationship with Appointment.
 */
@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * First name of the doctor (e.g., John, Priya).
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * Last name of the doctor (e.g., Smith, Verma).
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * Area of medical specialization (e.g., Cardiology, Dermatology).
     */
    @Column(name = "specialization")
    private String specialization;

    /**
     * One-to-One relationship with Clinic entity.
     * 
     * - This relationship is owned by the Doctor entity.
     * - @JoinColumn(name = "clinic_id") creates a foreign key in "doctor" table referencing "clinic".
     * - CascadeType.ALL ensures all lifecycle changes on Doctor also affect the Clinic.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    /**
     * One-to-Many relationship with Appointment entity.
     * 
     * - `mappedBy = "doctor"` refers to the `doctor` field in Appointment class.
     * -`Since @OneToMany defaults to Lazy Initialization it will cause error during fetch for Appointment entity along with doctor`
     * - EAGER fetching brings all appointments immediately with doctor (for demo/testing ease).
     * - Cascade types ensure appointments are persisted/merged/refreshed with doctor.
     * - REMOVE is not cascaded to avoid deleting appointments if doctor is removed.
     * - `@JsonManagedReference` helps handle serialization in bidirectional mapping.
     */
    @OneToMany(mappedBy = "doctor",
               fetch = FetchType.EAGER,
               cascade = {CascadeType.DETACH, CascadeType.MERGE,
                          CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonManagedReference
    private List<Appointment> appointments;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    // --- For debugging/logging ---

    @Override
    public String toString() {
        return "Doctor [id=" + id +
               ", firstName=" + firstName +
               ", lastName=" + lastName +
               ", specialization=" + specialization +
               ", clinic=" + (clinic != null ? clinic.getClinicName() : "N/A") +
               "]";
    }
}
