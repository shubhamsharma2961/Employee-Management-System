package com.company.ems.qualification;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

import com.company.ems.common.ApprovalStatus;
import com.company.ems.common.BaseEntity;
import com.company.ems.company.Company;
import com.company.ems.document.Document;
import com.company.ems.employee.Employee;
import com.company.ems.qualificationType.QualificationType;

@Entity
@Table(name = "qualifications")
public class Qualification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    @NotNull(message = "Employee is required")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "qualification_type_id", nullable = false)
    @NotNull(message = "Qualification type is required")
    private QualificationType qualificationType;

    @NotBlank(message = "Institution is required")
    @Size(min = 2, max = 150, message = "Institution must be between 2 and 150 characters")
    private String institution;

    @NotNull(message = "Year of completion is required")
    @Min(value = 1950, message = "Year is too old")
    @Max(value = 2100, message = "Year is invalid")
    private Integer yearOfCompletion;

    @NotBlank(message = "Grade is required")
    @Size(max = 20, message = "Grade must not exceed 20 characters")
    private String grade;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private ApprovalStatus status;

    @Size(max = 500, message = "Remarks must not exceed 500 characters")
    private String remarks;
    
    @OneToMany(mappedBy = "qualification", fetch = FetchType.LAZY)
    private List<Document> documents = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public QualificationType getQualificationType() {
		return qualificationType;
	}

	public void setQualificationType(QualificationType qualificationType) {
		this.qualificationType = qualificationType;
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

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
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

	public Qualification(@NotNull(message = "Employee is required") Employee employee,
			@NotNull(message = "Qualification type is required") QualificationType qualificationType,
			@NotBlank(message = "Institution is required") @Size(min = 2, max = 150, message = "Institution must be between 2 and 150 characters") String institution,
			@NotNull(message = "Year of completion is required") @Min(value = 1950, message = "Year is too old") @Max(value = 2100, message = "Year is invalid") Integer yearOfCompletion,
			@NotBlank(message = "Grade is required") @Size(max = 20, message = "Grade must not exceed 20 characters") String grade,
			List<Document> documents, @NotNull(message = "Status is required") ApprovalStatus status,
			@Size(max = 500, message = "Remarks must not exceed 500 characters") String remarks) {
		super();
		this.employee = employee;
		this.qualificationType = qualificationType;
		this.institution = institution;
		this.yearOfCompletion = yearOfCompletion;
		this.grade = grade;
		this.documents = documents;
		this.status = status;
		this.remarks = remarks;
	}
	public Qualification() {
		super();
	}
}
