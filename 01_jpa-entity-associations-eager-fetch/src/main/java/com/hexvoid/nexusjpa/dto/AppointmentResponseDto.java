package com.hexvoid.nexusjpa.dto;

/**
 * Response DTO for Appointment entity.
 * Used for sending appointment data in responses to the client.
 */
public class AppointmentResponseDto {

	private int id;                   // Unique ID of the appointment
	private String patientName;       // Patient name
	private String appointmentDate;   // Date of appointment (String format)

	// Doctor reference is intentionally commented to avoid infinite recursion or tight coupling
	// private DoctorResponseDto doctor;

	public AppointmentResponseDto() {}

	public AppointmentResponseDto(int id, String patientName, String appointmentDate) {
		this.id = id;
		this.patientName = patientName;
		this.appointmentDate = appointmentDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	//	public DoctorResponseDto getDoctor() {
	//		return doctor;
	//	}
	//
	//	public void setDoctor(DoctorResponseDto doctor) {
	//		this.doctor = doctor;
	//	}

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
}
