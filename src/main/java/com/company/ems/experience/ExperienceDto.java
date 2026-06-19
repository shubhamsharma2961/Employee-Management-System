package com.company.ems.experience;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.company.ems.common.ApprovalStatus;
import com.company.ems.document.DocumentDto;

import jakarta.validation.constraints.AssertTrue;

public class ExperienceDto {
	private UUID id;
    private UUID employeeId;
    private String employeeName;
    private String companyName;
    private String jobTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    @AssertTrue(message = "Start date must be before the end date")
    public boolean isDateRangeValid() {
        if (startDate == null || endDate == null) {
            return true; 
        }
        return !startDate.isAfter(endDate);
    }
    
    private String responsibilities;
    private ApprovalStatus status;
    private String remarks;
    private List<DocumentDto> documents = new ArrayList<>();
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public UUID getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(UUID employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
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
	public String getResponsibilities() {
		return responsibilities;
	}
	public void setResponsibilities(String responsibilities) {
		this.responsibilities = responsibilities;
	}
	public ApprovalStatus getStatus() {
		return status;
	}
	public void setStatus(ApprovalStatus status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public List<DocumentDto> getDocuments() {
		return documents;
	}
	public void setDocuments(List<DocumentDto> documents) {
		this.documents = documents;
	}
}
