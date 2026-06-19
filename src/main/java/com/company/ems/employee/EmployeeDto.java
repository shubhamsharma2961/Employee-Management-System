package com.company.ems.employee;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.company.ems.experience.ExperienceDto;
import com.company.ems.qualification.QualificationDto;

public class EmployeeDto {
	private UUID id;
    private String fullName;
    private String employeeCode;
    private String email;
    private String phoneNumber;
    private LocalDate hireDate;
    
//    private UUID companyId;
//    private String companyName;

    private UUID departmentId;
    private String departmentName;

    private UUID designationId;
    private String designationName;

    private UUID employeeTypeId;
    private String employeeTypeName;

    private UUID reportingManagerId;
    private String reportingManagerName;

    private UUID userId;
    private String username;
    private List<String> roles;
    
    private List<QualificationDto> qualifications;
    private List<ExperienceDto> experiences;
    
//	public UUID getCompanyId() {
//		return companyId;
//	}
//	public void setCompanyId(UUID companyId) {
//		this.companyId = companyId;
//	}
//	public String getCompanyName() {
//		return companyName;
//	}
//	public void setCompanyName(String companyName) {
//		this.companyName = companyName;
//	}
	public List<QualificationDto> getQualifications() {
		return qualifications;
	}
	public void setQualifications(List<QualificationDto> qualifications) {
		this.qualifications = qualifications;
	}
	public List<ExperienceDto> getExperiences() {
		return experiences;
	}
	public void setExperiences(List<ExperienceDto> experiences) {
		this.experiences = experiences;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
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
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public UUID getDesignationId() {
		return designationId;
	}
	public void setDesignationId(UUID designationId) {
		this.designationId = designationId;
	}
	public String getDesignationName() {
		return designationName;
	}
	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}
	public UUID getEmployeeTypeId() {
		return employeeTypeId;
	}
	public void setEmployeeTypeId(UUID employeeTypeId) {
		this.employeeTypeId = employeeTypeId;
	}
	public String getEmployeeTypeName() {
		return employeeTypeName;
	}
	public void setEmployeeTypeName(String employeeTypeName) {
		this.employeeTypeName = employeeTypeName;
	}
	public UUID getReportingManagerId() {
		return reportingManagerId;
	}
	public void setReportingManagerId(UUID reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}
	public String getReportingManagerName() {
		return reportingManagerName;
	}
	public void setReportingManagerName(String reportingManagerName) {
		this.reportingManagerName = reportingManagerName;
	}
	public UUID getUserId() {
		return userId;
	}
	public void setUserId(UUID userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
