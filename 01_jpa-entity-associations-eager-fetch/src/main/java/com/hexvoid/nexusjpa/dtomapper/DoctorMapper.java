package com.hexvoid.nexusjpa.dtomapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.hexvoid.nexusjpa.dto.AppointmentRequestDto;
import com.hexvoid.nexusjpa.dto.AppointmentResponseDto;
import com.hexvoid.nexusjpa.dto.ClinicRequestDto;
import com.hexvoid.nexusjpa.dto.ClinicResponseDto;
import com.hexvoid.nexusjpa.dto.DoctorRequestDto;
import com.hexvoid.nexusjpa.dto.DoctorResponseDto;
import com.hexvoid.nexusjpa.entity.Appointment;
import com.hexvoid.nexusjpa.entity.Clinic;
import com.hexvoid.nexusjpa.entity.Doctor;



/**
 * Utility class for mapping between {@link Doctor} entity and its related DTOs.
 * <p>
 * It provides conversion methods for:
 * <ul>
 *     <li>{@code Doctor ➜ DoctorRequestDto} (for request payloads)</li>
 *     <li>{@code DoctorRequestDto ➜ Doctor} (for persistence)</li>
 *     <li>{@code Doctor ➜ DoctorResponseDto} (for response payloads)</li>
 * </ul>
 */
public class DoctorMapper {

	/**
	 * Converts a {@link Doctor} entity into a {@link DoctorRequestDto}.
	 * <p>
     * This method is typically used to convert a Doctor entity into a structure suitable for client-side consumption.
	 *
	 * <p><b>Clinic Mapping:</b> A {@code ClinicRequestDto} is created from the nested {@code Clinic} entity.
	 * <br><b>Appointment Mapping:</b> Each {@code Appointment} is converted into a simplified {@code AppointmentRequestDto}.
	 *
	 * @param doctor the {@link Doctor} entity to be converted
	 * @return a fully populated {@link DoctorRequestDto}
	 */
	public static DoctorRequestDto toDto(Doctor doctor) {
		// Map clinic entity to ClinicRequestDto
		ClinicRequestDto clinicDto = new ClinicRequestDto(
				doctor.getClinic().getClinicName(),
				doctor.getClinic().getLocation()
				);

		// Map list of Appointment entities to list of AppointmentRequestDto
		List<AppointmentRequestDto> appointmentDtos = doctor.getAppointments()
				.stream()
				.map(app -> new AppointmentRequestDto(app.getPatientName(), app.getAppointmentDate()))
				.collect(Collectors.toList());

		// Return the DoctorRequestDto with clinic and appointments
		return new DoctorRequestDto(
				doctor.getFirstName(),
				doctor.getLastName(),
				doctor.getSpecialization(),
				clinicDto,
				appointmentDtos
				);
	}

	/**
	 * Converts a {@link DoctorRequestDto} into a {@link Doctor} entity.
	 * <p>
	 * This method is typically used when saving or updating data received from the client.
	 * Ensures bidirectional linking (Appointment ➜ Doctor) is properly set up.
	 * This ensures Hibernate/JPA cascades and joins work correctly when persisting nested entities.
	 *
	 * <p><b>Clinic Mapping:</b>
	 * <ul>
	 *     <li>Creates a new {@code Clinic} entity using values from {@code ClinicRequestDto}.</li>
	 *     <li>The {@code Clinic} is then linked to the {@code Doctor}.</li>
	 * </ul>
	 *
	 * <p><b>Appointment Mapping:</b>
	 * <ul>
	 *     <li>Each {@code AppointmentRequestDto} is transformed into a {@code Appointment} entity.</li>
	 *     <li>A back-reference is set on each appointment via {@code setDoctor(doctor)}.</li>
	 *     <li>This ensures a proper bidirectional relationship and prevents data inconsistency.</li>
	 * </ul>
	 *
	 * @param dto the {@link DoctorRequestDto} to convert
	 * @return a {@link Doctor} entity populated with clinic and appointments
	 */
	public static Doctor toEntity(DoctorRequestDto dto) {
		// Create and populate Clinic entity from ClinicDto
		Clinic clinic = new Clinic();
		clinic.setClinicName(dto.getClinic().getClinicName());
		clinic.setLocation(dto.getClinic().getLocation());

		// Create and populate Doctor entity
		Doctor doctor = new Doctor();
		doctor.setFirstName(dto.getFirstName());
		doctor.setLastName(dto.getLastName());
		doctor.setSpecialization(dto.getSpecialization());
		doctor.setClinic(clinic);  // Associate clinic with doctor

		// Convert AppointmentDto list to Appointment entity list
		List<Appointment> appointmentList = new ArrayList<>();

		for (AppointmentRequestDto appointmentDto : dto.getAppointments()) {
			Appointment appointment = new Appointment();
			appointment.setPatientName(appointmentDto.getPatientName());
			appointment.setAppointmentDate(appointmentDto.getAppointmentDate());

			/**
			 *  BACK-REFERENCE:
			 * This line is critical. It sets the parent doctor inside each appointment.
			 * Without this, the bidirectional mapping @ManyToOne from Appointment ➜ Doctor)
			 * would remain incomplete and the `doctor_id` in the database would be null.
			 *
			 * This ensures the object graph is fully wired before persistence.
			 */
			appointment.setDoctor(doctor);

			appointmentList.add(appointment);
		}
		// Set all appointments into the doctor
		doctor.setAppointments(appointmentList);



		/**
		 *  ALTERNATIVE APPROACH USING STREAMS
		 * You can also achieve the same result using a cleaner functional approach:
		 *
		 **/
		
		//		List<Appointment> appointments = dto.getAppointments()
		//				.stream()
		//				.map(a -> {
		//					Appointment app = new Appointment();
		//					app.setPatientName(a.getPatientName());
		//					app.setAppointmentDate(a.getAppointmentDate());
		//					app.setDoctor(doctor); // Back-reference
		//					return app;
		//				}).collect(Collectors.toList());
		//		doctor.setAppointments(appointments);



		return doctor;
	}
	
	/**
	 * Converts a {@link Doctor} entity into a {@link DoctorResponseDto} for API responses.
	 * <p>
	 * This method is used when sending full doctor details to the client including ID values.
	 *
	 * <p><b>Features:</b>
	 * <ul>
	 *     <li>Includes clinic and appointment details</li>
	 *     <li>Returns only response-safe objects (e.g. hides internal DB structure)</li>
	 * </ul>
	 *
	 * @param doctor the {@link Doctor} entity to convert
	 * @return the corresponding {@link DoctorResponseDto}
	 */
	
	public DoctorResponseDto toDoctorResponseDto(Doctor doctor) {
		// Create response body and populate basic fields
		DoctorResponseDto responseBody = new DoctorResponseDto();
		responseBody.setId(doctor.getId());
		responseBody.setFirstName(doctor.getFirstName());
		responseBody.setLastName(doctor.getLastName());
		responseBody.setSpecialization(doctor.getSpecialization());
		
		// Map clinic entity to ClinicResponseDto
		ClinicResponseDto clinic = new ClinicResponseDto(
				doctor.getId(), 
				doctor.getClinic().getClinicName(), 
				doctor.getClinic().getLocation()
		);
		responseBody.setClinic(clinic);
		
		// Map appointments to AppointmentResponseDto list
		List<AppointmentResponseDto> appointmentsResponseList = doctor.getAppointments()
				.stream()
				.map(appointments -> new AppointmentResponseDto(
						appointments.getId(),
						appointments.getPatientName(),
						appointments.getAppointmentDate()
						))
				.collect(Collectors.toList());
		responseBody.setAppointments(appointmentsResponseList);
		
		return responseBody;
		
	}
}
