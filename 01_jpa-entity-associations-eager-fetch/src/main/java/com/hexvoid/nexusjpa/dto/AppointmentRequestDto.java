package com.hexvoid.nexusjpa.dto;

/**
 * DTO used for creating or updating an appointment.
 * This is a simplified representation sent in requests.
 */
public class AppointmentRequestDto {

    private String patientName;       // Name of the patient booking the appointment
    private String appointmentDate;   // Appointment date in String format (e.g., "2025-04-12")

    public AppointmentRequestDto() {}

    public AppointmentRequestDto(String patientName, String appointmentDate) {
        this.patientName = patientName;
        this.appointmentDate = appointmentDate;
    }

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
