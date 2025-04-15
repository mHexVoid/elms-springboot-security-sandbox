package com.hexvoid.nexusjpa.dto;

import java.util.List;

/**
 * Response DTO for Doctor entity.
 * Used to send doctor details along with associated clinic and appointment info.
 */
public class DoctorResponseDto {

	private int id;                                  // Unique ID of the doctor
	private String firstName;                        // Doctor's first name
    private String lastName;                         // Doctor's last name
    private String specialization;                   // Medical field
    private ClinicResponseDto clinic;                // Associated clinic info
    private List<AppointmentResponseDto> appointments; // List of appointments for the doctor

    public DoctorResponseDto() {}

    public DoctorResponseDto(int id, String firstName, String lastName, String specialization,
                     ClinicResponseDto clinic, List<AppointmentResponseDto> appointments) {
    	this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.clinic = clinic;
        this.appointments = appointments;
    }
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

    public ClinicResponseDto getClinic() {
        return clinic;
    }

    public void setClinic(ClinicResponseDto clinic) {
        this.clinic = clinic;
    }

    public List<AppointmentResponseDto> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentResponseDto> appointments) {
        this.appointments = appointments;
    }
}
