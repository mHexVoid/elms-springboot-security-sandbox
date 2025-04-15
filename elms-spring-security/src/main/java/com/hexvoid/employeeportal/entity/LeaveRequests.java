package com.hexvoid.employeeportal.entity;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents a leave request submitted by an employee.
 * 
 * This entity maps to the `leave_requests` table in the database.
 * It tracks employee leave details like type, status, start and end dates.
 * Uses Hibernate and JPA annotations to manage the database schema.
 */
@Entity // Marks this class as a JPA entity (table in DB)
@Table(name = "leave_requests") // Maps this entity to the 'leave_requests' table
public class LeaveRequests {

    /**
     * PRIMARY KEY
     * - `@Id`: Marks this field as the primary key.
     * - `@GeneratedValue(strategy = GenerationType.IDENTITY)`: Uses AUTO_INCREMENT in MySQL.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /**
     * RELATIONSHIP WITH EMPLOYEES
     * - `@ManyToOne`: Many leave requests can belong to one employee.
     * - `@JoinColumn(name = "employee_id", nullable = false)`: Stores the foreign key reference.
     */
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employees employee;

    /**
     *    LEAVE TYPE
     * - `@Enumerated(EnumType.STRING)`: Stores the enum value as a String.
     * - Ensures that leave type is stored as 'SICK', 'CASUAL', or 'EARNED' in the database.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type", nullable = false)
    private LeaveType leaveType;

    /**
     *    START AND END DATES
     * - `LocalDate` is used instead of `Date` to store date-only values.
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    /**
     *   LEAVE STATUS
     * - `@Enumerated(EnumType.STRING)`: Stores the enum value as a String.
     * - Default value is `PENDING`.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LeaveStatus leaveStatus = LeaveStatus.PENDING;

    /**
     *   REASON FOR LEAVE
     * - Stored as `TEXT` to allow long descriptions.
     */
    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    /**
     *    CREATED AT TIMESTAMP (AUTOMATIC)
     * - `@CreationTimestamp`: Automatically sets timestamp when record is inserted.
     * - `updatable = false`: This field is not updated after creation.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    /**
     *   UPDATED AT TIMESTAMP (AUTOMATIC)
     * - `@UpdateTimestamp`: Automatically updates when record is modified.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    //  Default Constructor (JPA requires a no-argument constructor)
    public LeaveRequests() {}

    //  Getters and Setters
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public LeaveType getLeaveType() {
        return leaveType;
    }

	public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LeaveStatus getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(LeaveStatus leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Employees getEmployee() {
        return employee;
    }

    public void setEmployee(Employees employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "LeaveRequests{" +
                "id=" + id +
                ", leaveType=" + leaveType +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", leaveStatus=" + leaveStatus +
                ", reason='" + reason + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", employee=" + employee +
                '}';
    }
}
