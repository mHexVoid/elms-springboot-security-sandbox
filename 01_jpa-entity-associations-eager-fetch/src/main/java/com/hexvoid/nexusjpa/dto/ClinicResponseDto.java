package com.hexvoid.nexusjpa.dto;

/**
 * DTO used to send clinic details as part of the API response.
 * Unlike ClinicDto, it includes the database ID field.
 */
public class ClinicResponseDto {

	private int id;                 // Unique identifier of the clinic
	private String clinicName;      // Clinic name
	private String location;        // Address or area of the clinic

	// Doctor reference is excluded to avoid circular dependency or unnecessary data in response
	// private DoctorResponseDto doctor;

	public ClinicResponseDto() {}

	public ClinicResponseDto(int id , String clinicName, String location) {
		this.id = id;
		this.clinicName = clinicName;
		this.location = location;
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
}
