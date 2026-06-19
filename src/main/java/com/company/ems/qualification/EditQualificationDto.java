package com.company.ems.qualification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EditQualificationDto {
	@NotNull(message = "Qualification type category selection is required")
    private UUID qualificationTypeId;

    @NotBlank(message = "Institution name is required")
    @Size(min = 2, max = 150, message = "Institution name must be between 2 and 150 characters")
    private String institution;

    @NotNull(message = "Year of completion tracking is required")
    @Min(value = 1950, message = "Year of completion cannot be older than 1950")
    @Max(value = 2100, message = "Year of completion is out of standard operational bounds")
    private Integer yearOfCompletion;

    @NotBlank(message = "Grade or evaluation mark is required")
    @Size(max = 20, message = "Grade evaluation text must not exceed 20 characters")
    private String grade;

    private List<UUID> documentIds = new ArrayList<>();

    @Size(max = 500, message = "Remarks must not exceed 500 characters")
    private String remarks;

    public EditQualificationDto() {}

	public UUID getQualificationTypeId() {
		return qualificationTypeId;
	}

	public void setQualificationTypeId(UUID qualificationTypeId) {
		this.qualificationTypeId = qualificationTypeId;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public Integer getYearOfCompletion() {
		return yearOfCompletion;
	}

	public void setYearOfCompletion(Integer yearOfCompletion) {
		this.yearOfCompletion = yearOfCompletion;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public List<UUID> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<UUID> documentIds) {
		this.documentIds = documentIds;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
