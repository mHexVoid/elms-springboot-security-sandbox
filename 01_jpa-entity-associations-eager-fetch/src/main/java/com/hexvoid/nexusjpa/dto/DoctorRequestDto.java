package com.hexvoid.nexusjpa.dto;

import java.util.List;

/**
 * DTO used for creating or updating Doctor information.
 * Includes embedded clinic and appointment data for cascade persistence.
 */
public class DoctorRequestDto {

    private String firstName;                      // First name of the doctor
    private String lastName;                       // Last name of the doctor
    private String specialization;                 // Doctor's medical specialization
    private ClinicRequestDto clinic;                      // Associated clinic info
    private List<AppointmentRequestDto> appointments;     // List of appointments to be created along with doctor

    public DoctorRequestDto() {}

    public DoctorRequestDto(String firstName, String lastName, String specialization,
                     ClinicRequestDto clinic, List<AppointmentRequestDto> appointments) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.clinic = clinic;
        this.appointments = appointments;
    }

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

    public ClinicRequestDto getClinic() {
        return clinic;
    }

    public void setClinic(ClinicRequestDto clinic) {
        this.clinic = clinic;
    }

    public List<AppointmentRequestDto> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentRequestDto> appointments) {
        this.appointments = appointments;
    }
}
