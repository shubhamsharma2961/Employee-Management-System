package com.company.ems.qualification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.company.ems.common.ApprovalStatus;
import com.company.ems.document.DocumentDto;

public class QualificationDto {
	private UUID id;
    private UUID employeeId;
    private String employeeName;
    private UUID qualificationTypeId;
    private String qualificationTypeName;
    private String institution;
    private Integer yearOfCompletion;
    private String grade;
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
	public UUID getQualificationTypeId() {
		return qualificationTypeId;
	}
	public void setQualificationTypeId(UUID qualificationTypeId) {
		this.qualificationTypeId = qualificationTypeId;
	}
	public String getQualificationTypeName() {
		return qualificationTypeName;
	}
	public void setQualificationTypeName(String qualificationTypeName) {
		this.qualificationTypeName = qualificationTypeName;
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
