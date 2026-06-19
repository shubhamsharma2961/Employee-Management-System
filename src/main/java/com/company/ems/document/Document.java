package com.company.ems.document;

import com.company.ems.common.BaseEntity;
import com.company.ems.company.Company;
import com.company.ems.documentType.DocumentType;
import com.company.ems.experience.Experience;
import com.company.ems.qualification.Qualification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "documents")
public class Document extends BaseEntity{
	@NotBlank(message = "File name is required")
    @Column(name = "file_name", nullable = false)
    @Pattern(regexp = "([^\\s]+(\\.(?i)(pdf|png|jpg|jpeg))$)", 
             message = "File extension must be either .pdf, .png, .jpg, or .jpeg")
    private String fileName;

    @NotBlank(message = "File storage path path cannot be empty")
    @Column(name = "file_path", nullable = false)
    private String filePath;

    @NotBlank(message = "File type/MIME type is required")
    @Column(name = "file_type", nullable = false)
    @Pattern(regexp = "^(?i)(application/pdf|image/png|image/jpeg|image/jpg|pdf|png|jpg|jpeg)$",
             message = "Invalid file type format. Only PDF, PNG, JPG, and JPEG files are allowed.")
    private String fileType;

    @NotNull(message = "File size metric tracking is required")
    @Positive(message = "File size must be greater than 0 bytes")
    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_type_id", nullable = true)
    private DocumentType documentType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qualification_id")
    private Qualification qualification;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experience_id")
    private Experience experience;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Experience getExperience() {
		return experience;
	}

	public void setExperience(Experience experience) {
		this.experience = experience;
	}

	public Qualification getQualification() {
		return qualification;
	}

	public void setQualification(Qualification qualification) {
		this.qualification = qualification;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

	public Document() {}

	public Document(
			@NotBlank(message = "File name is required") @Pattern(regexp = "([^\\s]+(\\.(?i)(pdf|png|jpg|jpeg))$)", message = "File extension must be either .pdf, .png, .jpg, or .jpeg") String fileName,
			@NotBlank(message = "File storage path path cannot be empty") String filePath,
			@NotBlank(message = "File type/MIME type is required") @Pattern(regexp = "^(?i)(application/pdf|image/png|image/jpeg|image/jpg|pdf|png|jpg|jpeg)$", message = "Invalid file type format. Only PDF, PNG, JPG, and JPEG files are allowed.") String fileType,
			@NotNull(message = "File size metric tracking is required") @Positive(message = "File size must be greater than 0 bytes") Long fileSize,
			@NotNull(message = "Document must be mapped to an authorized system DocumentType category") DocumentType documentType) {
		super();
		this.fileName = fileName;
		this.filePath = filePath;
		this.fileType = fileType;
		this.fileSize = fileSize;
		this.documentType = documentType;
	}
    
}
