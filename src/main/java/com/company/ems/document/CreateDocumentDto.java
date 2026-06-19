package com.company.ems.document;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class CreateDocumentDto {

    @NotBlank(message = "File name is required")
    @Pattern(regexp = "([^\\s]+(\\.(?i)(pdf|png|jpg|jpeg))$)", 
             message = "File extension must be either .pdf, .png, .jpg, or .jpeg")
    private String fileName;

    @NotBlank(message = "File path is required")
    private String filePath;

    @NotBlank(message = "File type is required")
    @Pattern(regexp = "^(?i)(application/pdf|image/png|image/jpeg|image/jpg|pdf|png|jpg|jpeg)$",
             message = "Only PDF, PNG, JPG, and JPEG file types are allowed")
    private String fileType;

    @NotNull(message = "File size is required")
    @Positive(message = "File size must be greater than 0")
    private Long fileSize;

    @NotNull(message = "Document Type ID association is required")
    private UUID documentTypeId;

	public CreateDocumentDto() {}

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

	public UUID getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(UUID documentTypeId) {
		this.documentTypeId = documentTypeId;
	}
    
}
