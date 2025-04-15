package com.hexvoid.nexusjpa.dto;

/**
 * DTO for sending clinic data in creation or update requests.
 * Keeps the request structure clean and avoids exposing internal IDs or relationships.
 */
public class ClinicRequestDto {

    private String clinicName;    // Name of the clinic
    private String location;      // Address or general location

    public ClinicRequestDto() {}

    public ClinicRequestDto(String clinicName, String location) {
        this.clinicName = clinicName;
        this.location = location;
    }

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
