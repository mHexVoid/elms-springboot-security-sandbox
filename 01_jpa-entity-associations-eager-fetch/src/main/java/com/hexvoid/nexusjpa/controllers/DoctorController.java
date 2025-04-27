package com.hexvoid.nexusjpa.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexvoid.nexusjpa.dao.DoctorDAO;
import com.hexvoid.nexusjpa.dto.AppointmentResponseDto;
import com.hexvoid.nexusjpa.dto.ClinicResponseDto;
import com.hexvoid.nexusjpa.dto.DoctorRequestDto;
import com.hexvoid.nexusjpa.dto.DoctorResponseDto;
import com.hexvoid.nexusjpa.dtomapper.DoctorMapper;
import com.hexvoid.nexusjpa.entity.Appointment;
import com.hexvoid.nexusjpa.entity.Clinic;
import com.hexvoid.nexusjpa.entity.Doctor;
import com.hexvoid.nexusjpa.exceptionhandler.BusinessException;

/**
 * REST controller for handling Doctor-related API endpoints.
 * <p>
 * Provides operations for:
 * <ul>
 *     <li>Saving doctor data including clinic and appointments</li>
 *     <li>Fetching doctor information in different DTO formats</li>
 * </ul>
 * 
 * Base URL: /api/clinic
 */
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

	private final DoctorDAO doctorDAO;

	// Constructor-based dependency injection of DoctorDAO
	public DoctorController(DoctorDAO doctorDAO) {
		this.doctorDAO = doctorDAO;
	}

	/**
	 * Test endpoint to check if controller is up.
	 */
	@GetMapping("/ping")
	public ResponseEntity<String> ping() {
		return ResponseEntity.ok("DoctorController is up and running!");
	}

	/**
	 * Saves a doctor including nested clinic and appointment data.
	 * <p>
	 * This method maps the incoming {@link DoctorRequestDto} to a {@link Doctor} entity
	 * and persists it using the DAO layer.
	 *
	 * @param doctorDto the doctor details from request body
	 * @return the saved {@link Doctor} entity
	 */
	@PostMapping
	public Doctor saveDoctor(@RequestBody DoctorRequestDto doctorDto) {
		System.out.println("Received Doctor: " + doctorDto.toString());
		System.out.println("Clinic: " + doctorDto.getClinic().toString());
		System.out.println("Appointments: " + doctorDto.getAppointments().toString());

		Doctor doctor = DoctorMapper.toEntity(doctorDto);
		Doctor savedDoctor = doctorDAO.saveDoctor(doctor);

		System.out.println("Saved Doctor: " + savedDoctor);
		return savedDoctor;
	}

	/**
	 * Saves a doctor using {@link ResponseEntity} to wrap the response.
	 * <p>
	 * Returns 201 CREATED status upon successful creation.
	 *
	 * @param doctorDto the doctor details from request
	 * @return response entity containing the saved doctor
	 */
	@PostMapping("/with-response")
	public ResponseEntity<Doctor> saveDoctorResponse(@RequestBody DoctorRequestDto doctorDto) {
		Doctor doctor = DoctorMapper.toEntity(doctorDto);
		Doctor saved = doctorDAO.saveDoctor(doctor);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	/**
	 * Retrieves a doctor with their associated appointments by doctor ID.
	 * <p>
	 * Uses {@link DoctorRequestDto} to return readable data with clinic and appointments.
	 *
	 * @param id the doctor ID
	 * @return doctor data as {@link DoctorDto}
	 */
	@GetMapping("/{doctorId}/details")
	public ResponseEntity<DoctorRequestDto> findDoctorWithAppointments(@PathVariable int doctorId) {
		Doctor doctor = doctorDAO.findDoctorWithAppointments(doctorId);
		DoctorRequestDto dto = DoctorMapper.toDto(doctor);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	/**
	 * Retrieves a doctor in a response-specific DTO format including IDs.
	 * <p>
	 * This is ideal for frontend/UI consumption where unique identifiers are necessary.
	 *
	 * @param id the doctor ID
	 * @return the {@link DoctorResponseDto} wrapped in a response entity
	 */
	@GetMapping("/{doctorId}/response")
	public ResponseEntity<DoctorResponseDto> findDoctorByIdHavingAppointMents(@PathVariable int doctorId){
		Doctor doctor = doctorDAO.findDoctorWithAppointments(doctorId);

		// NOTE: Consider refactoring to make toDoctorResponseDto() static for consistency.
		DoctorMapper invokeTheNonStaticMethodWithRef = new DoctorMapper();
		DoctorResponseDto responseDto = invokeTheNonStaticMethodWithRef.toDoctorResponseDto(doctor);

		return ResponseEntity.ok(responseDto);

	}

	@PutMapping("/{doctorId}/appointments/ordered")
	public ResponseEntity<DoctorResponseDto> updateDoctorById( @PathVariable int doctorId , @RequestBody DoctorResponseDto doctorRequestDto){

		// Validate that path variable and request body ID are consistent
		if (doctorId != doctorRequestDto.getId()) {
			throw new BusinessException("Requested Doctor ID and provided Doctor ID must match.", HttpStatus.BAD_REQUEST);
		}

		// Fetch doctor; now Hibernate will track changes automatically (Managed State).
		Doctor existingDoctor = doctorDAO.findDoctorWithAppointments(doctorId);

		// Update basic doctor details
		existingDoctor.setFirstName(doctorRequestDto.getFirstName());
		existingDoctor.setLastName(doctorRequestDto.getLastName());
		existingDoctor.setSpecialization(doctorRequestDto.getSpecialization());

		// Update associated clinic details
		existingDoctor.getClinic().setClinicName(doctorRequestDto.getClinic().getClinicName());
		existingDoctor.getClinic().setLocation(doctorRequestDto.getClinic().getLocation());
		// maintain bidirectional relationship
		existingDoctor.getClinic().setDoctor(existingDoctor);

		// Prepare to update appointments
		List<Appointment> existingAppointments = existingDoctor.getAppointments();
		List<AppointmentResponseDto> incomingAppointments = doctorRequestDto.getAppointments();

		// Iterate over the incoming appointments
		for(int j =0 ; j<incomingAppointments.size();j++) {

			// Create a new Appointment object (if needed later)
			Appointment newAppointment = new Appointment();

			//Updating Value in ExistingDBList Till Size are equal Incoming List to Avoid IndexOutofBoundException
			if(existingAppointments.size() > j) {

				//Checking if appointment IDs match
				if(	existingAppointments.get(j).getId() == incomingAppointments.get(j).getId())
				{
					// Update the existing appointment fields
					existingAppointments.get(j).setAppointmentDate(
							incomingAppointments.get(j).getAppointmentDate()
							);
					existingAppointments.get(j).setPatientName(
							incomingAppointments.get(j).getPatientName()
							);
					existingAppointments.get(j).setDoctor(existingDoctor);
					// Move to next iteration to avoid duplicate addition
					continue;
				}
			}
			// Else, create a new appointment from incoming extra data
			newAppointment.setAppointmentDate(incomingAppointments.get(j).getAppointmentDate());
			newAppointment.setPatientName(incomingAppointments.get(j).getPatientName());
			newAppointment.setDoctor(existingDoctor);

			// Add the new appointment to existing list
			existingAppointments.add(newAppointment);	
		}

		// Save the updated doctor entity
		Doctor updateDoctor = doctorDAO.saveDoctor(existingDoctor);

		// Map updated doctor entity to response DTO
		DoctorMapper responsedto = new DoctorMapper();
		DoctorResponseDto doctorResponseDto = responsedto.toDoctorResponseDto(updateDoctor);

		return new ResponseEntity<>(doctorResponseDto, HttpStatus.OK);
	}

	@PutMapping("/{doctorId}/appointments/unordered")
	public ResponseEntity<DoctorResponseDto> updateDoctorByIdv(
			@PathVariable int doctorId, 
			@RequestBody DoctorResponseDto doctorRequestDto) {

		// Validate that path variable and request body ID are consistent
		if (doctorId != doctorRequestDto.getId()) {
			throw new BusinessException("Requested Doctor ID and provided Doctor ID must match.", HttpStatus.BAD_REQUEST);
		}

		// Fetch existing doctor along with associated appointments and clinic details
		Doctor existingDoctor = doctorDAO.findDoctorWithAppointments(doctorId);

		// Update basic doctor details
		existingDoctor.setFirstName(doctorRequestDto.getFirstName());
		existingDoctor.setLastName(doctorRequestDto.getLastName());
		existingDoctor.setSpecialization(doctorRequestDto.getSpecialization());

		// Update associated clinic details
		existingDoctor.getClinic().setClinicName(doctorRequestDto.getClinic().getClinicName());
		existingDoctor.getClinic().setLocation(doctorRequestDto.getClinic().getLocation());
		existingDoctor.getClinic().setDoctor(existingDoctor); // maintain bidirectional relationship

		// Prepare to update appointments
		List<Appointment> existingAppointments = existingDoctor.getAppointments();
		List<AppointmentResponseDto> incomingAppointments = doctorRequestDto.getAppointments();

		// Create a Map for faster lookup of existing appointments by ID
		 Map<Integer, Appointment> existingAppointmentMap = new HashMap<>();
		    for (Appointment appointment : existingAppointments) {
		        existingAppointmentMap.put(appointment.getId(), appointment);
		    }
		    
		//OR
//		Map<Integer, Appointment> existingAppointmentMap = existingAppointments.stream()
//				.collect(Collectors.toMap(Appointment::getId, appointment -> appointment));

		for (AppointmentResponseDto incomingAppointmentDto : incomingAppointments) {
			Appointment appointmentToUpdate = existingAppointmentMap.get(incomingAppointmentDto.getId());

			if (appointmentToUpdate != null) {
				// Existing appointment found: update its details
				appointmentToUpdate.setAppointmentDate(incomingAppointmentDto.getAppointmentDate());
				appointmentToUpdate.setPatientName(incomingAppointmentDto.getPatientName());
				appointmentToUpdate.setDoctor(existingDoctor); // maintain relationship
			} else {
				// New appointment: create and add to the list
				Appointment newAppointment = new Appointment();
				newAppointment.setAppointmentDate(incomingAppointmentDto.getAppointmentDate());
				newAppointment.setPatientName(incomingAppointmentDto.getPatientName());
				newAppointment.setDoctor(existingDoctor); // maintain relationship

				existingAppointments.add(newAppointment);
			}
		}

		// Persist updated doctor entity
		Doctor updatedDoctor = doctorDAO.saveDoctor(existingDoctor);

		// Convert entity back to response DTO
		DoctorMapper doctorMapper = new DoctorMapper();
		DoctorResponseDto doctorResponseDto = doctorMapper.toDoctorResponseDto(updatedDoctor);

		return new ResponseEntity<>(doctorResponseDto, HttpStatus.OK);
	}

	@PutMapping("/{doctorId}/appointments/ignore-new")
	public ResponseEntity<DoctorResponseDto> updateDoctorByIdIgnoreNewAppointment(
			@PathVariable int doctorId, 
			@RequestBody DoctorResponseDto doctorRequestDto) {

		// Validate that path variable and request body ID are consistent
		if (doctorId != doctorRequestDto.getId()) {
			throw new BusinessException("Requested Doctor ID and provided Doctor ID must match.", HttpStatus.BAD_REQUEST);
		}

		// Fetch existing doctor along with associated appointments and clinic details
		Doctor existingDoctor = doctorDAO.findDoctorWithAppointments(doctorId);

		// Update basic doctor details
		existingDoctor.setFirstName(doctorRequestDto.getFirstName());
		existingDoctor.setLastName(doctorRequestDto.getLastName());
		existingDoctor.setSpecialization(doctorRequestDto.getSpecialization());

		// Update associated clinic details
		existingDoctor.getClinic().setClinicName(doctorRequestDto.getClinic().getClinicName());
		existingDoctor.getClinic().setLocation(doctorRequestDto.getClinic().getLocation());
		existingDoctor.getClinic().setDoctor(existingDoctor); // maintain bidirectional relationship

		// Prepare to update appointments
		List<Appointment> existingAppointments = existingDoctor.getAppointments();
		List<AppointmentResponseDto> incomingAppointments = doctorRequestDto.getAppointments();

		if(existingAppointments.size()!= incomingAppointments.size()) {
			throw new BusinessException("Sorry New Appointment Window is closed now", HttpStatus.BAD_REQUEST);
		}

		// Create a Map for faster lookup of existing appointments by ID
		Map<Integer, Appointment> existingAppointmentMap = existingAppointments.stream()
				.collect(Collectors.toMap(Appointment::getId, appointment -> appointment));

		for (AppointmentResponseDto incomingAppointmentDto : incomingAppointments) {
			Appointment appointmentToUpdate = existingAppointmentMap.get(incomingAppointmentDto.getId());

			if (appointmentToUpdate != null) {
				// Existing appointment found: update its details
				appointmentToUpdate.setAppointmentDate(incomingAppointmentDto.getAppointmentDate());
				appointmentToUpdate.setPatientName(incomingAppointmentDto.getPatientName());
				appointmentToUpdate.setDoctor(existingDoctor); // maintain relationship
			} else {
				// New appointment: create and add to the list
				Appointment newAppointment = new Appointment();
				newAppointment.setAppointmentDate(incomingAppointmentDto.getAppointmentDate());
				newAppointment.setPatientName(incomingAppointmentDto.getPatientName());
				newAppointment.setDoctor(existingDoctor); // maintain relationship

				existingAppointments.add(newAppointment);
			}
		}

		// Persist updated doctor entity
		Doctor updatedDoctor = doctorDAO.saveDoctor(existingDoctor);

		// Convert entity back to response DTO
		DoctorMapper doctorMapper = new DoctorMapper();
		DoctorResponseDto doctorResponseDto = doctorMapper.toDoctorResponseDto(updatedDoctor);

		return new ResponseEntity<>(doctorResponseDto, HttpStatus.OK);
	}



	@DeleteMapping("/{doctorId}")
	public ResponseEntity<String> deleteDoctorById( @PathVariable int doctorId){

		doctorDAO.deleteDoctor(doctorId);

		return new ResponseEntity<>("Deleted Doctor Successfully",HttpStatus.OK);

	}

}
