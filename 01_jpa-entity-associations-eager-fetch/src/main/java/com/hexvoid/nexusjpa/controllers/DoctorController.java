package com.hexvoid.nexusjpa.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexvoid.nexusjpa.dao.DoctorDAO;
import com.hexvoid.nexusjpa.dto.DoctorRequestDto;
import com.hexvoid.nexusjpa.dto.DoctorResponseDto;
import com.hexvoid.nexusjpa.dtomapper.DoctorMapper;
import com.hexvoid.nexusjpa.entity.Doctor;

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
@RequestMapping("/api/clinic")
public class DoctorController {

    private final DoctorDAO doctorDAO;

    // Constructor-based dependency injection of DoctorDAO
    public DoctorController(DoctorDAO doctorDAO) {
        this.doctorDAO = doctorDAO;
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
    @PostMapping("/save/doctor")
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
    @PostMapping("/save/doctor/response")
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
    @GetMapping("/get/doctor/withappointments/{id}")
    public ResponseEntity<DoctorRequestDto> findDoctorWithAppointments(@PathVariable int id) {
       Doctor doctor = doctorDAO.findDoctorWithAppointments(id);
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
    @GetMapping("/get/doctor/with/response/dto/{id}")
    public ResponseEntity<DoctorResponseDto> findDoctorByIdHavingAppointMents(@PathVariable int id){
    	Doctor doctor = doctorDAO.findDoctorWithAppointments(id);
    	
        // NOTE: Consider refactoring to make toDoctorResponseDto() static for consistency.
    	DoctorMapper invokeTheNonStaticMethodWithRef = new DoctorMapper();
    	DoctorResponseDto responseDto = invokeTheNonStaticMethodWithRef.toDoctorResponseDto(doctor);
    	
    	return ResponseEntity.ok(responseDto);
    	
    }
    
}
