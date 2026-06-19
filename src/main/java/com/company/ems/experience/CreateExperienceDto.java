package com.company.ems.experience;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateExperienceDto {

    @NotBlank(message = "Company name is required")
    @Size(max = 150, message = "Company name must not exceed 150 characters")
    private String companyName;

    @NotBlank(message = "Job title is required")
    @Size(max = 100, message = "Job title must not exceed 100 characters")
    private String jobTitle;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate; 
    
    @AssertTrue(message = "Start date must be before the end date")
    public boolean isDateRangeValid() {
        if (startDate == null || endDate == null) {
            return true; 
        }
        return !startDate.isAfter(endDate);
    }

    @Size(max = 2000, message = "Responsibilities narrative description must not exceed 2000 characters")
    private String responsibilities;

    private List<UUID> documentIds = new ArrayList<>();

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

	public List<UUID> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<UUID> documentIds) {
		this.documentIds = documentIds;
	}
}