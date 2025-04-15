package com.hexvoid.nexusjpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Represents a Clinic where a Doctor practices.
 * 
 * This entity maps to the "clinic" table in the database.
 * It forms the inverse side of a bidirectional One-to-One relationship with the Doctor entity.
 * 
 * - Used to store clinic-specific metadata such as the clinic's name and its location.
 */
@Entity
@Table(name = "clinic")
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * The name of the clinic (e.g., Sunrise Health Center, Medilife Clinic).
     */
    @Column(name = "clinic_name")
    private String clinicName;

    /**
     * The geographical location/address of the clinic (e.g., New Delhi, Mumbai).
     */
    @Column(name = "location")
    private String location;

    /**
     * Bidirectional One-to-One relationship with Doctor entity.
     * 
     * - `mappedBy = "clinic"` means the Doctor entity owns the relationship.
     * - Cascades: 
     *   - DETACH, MERGE, PERSIST, REFRESH are allowed to propagate related changes.
     *   - REMOVE is intentionally excluded to avoid deleting the Doctor when a Clinic is deleted.
     * - `@JsonIgnore` avoids infinite recursion during JSON serialization.
     */
    @JsonIgnore
    @OneToOne(mappedBy = "clinic",
              cascade = {CascadeType.DETACH, CascadeType.MERGE,
                         CascadeType.PERSIST, CascadeType.REFRESH})
    private Doctor doctor;

    // Getters and Setters

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


    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    /**
     * Returns a string representation of the Clinic,
     * excluding the associated doctor to avoid circular references.
     */
    @Override
    public String toString() {
        return "Clinic [id=" + id + ", clinicName=" + clinicName + ", location=" + location + "]";
    }
}
