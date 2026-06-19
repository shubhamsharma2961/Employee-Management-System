package com.company.ems.employee;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateEmployeeDto {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "Hire date is required")
    private LocalDate hireDate;

    @NotNull(message = "Department is required")
    private UUID departmentId;

    @NotNull(message = "Designation is required")
    private UUID designationId;

    @NotNull(message = "Employee type is required")
    private UUID employeeTypeId;

    private UUID reportingManagerId; 

    @NotNull(message = "Role is required for login creation")
    private UUID roleId;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public LocalDate getHireDate() {
		return hireDate;
	}

	public void setHireDate(LocalDate hireDate) {
		this.hireDate = hireDate;
	}

	public UUID getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(UUID departmentId) {
		this.departmentId = departmentId;
	}

	public UUID getDesignationId() {
		return designationId;
	}

	public void setDesignationId(UUID designationId) {
		this.designationId = designationId;
	}

	public UUID getEmployeeTypeId() {
		return employeeTypeId;
	}

	public void setEmployeeTypeId(UUID employeeTypeId) {
		this.employeeTypeId = employeeTypeId;
	}

	public UUID getReportingManagerId() {
		return reportingManagerId;
	}

	public void setReportingManagerId(UUID reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}

	public UUID getRoleId() {
		return roleId;
	}

	public void setRoleId(UUID roleId) {
		this.roleId = roleId;
	}
   
}
