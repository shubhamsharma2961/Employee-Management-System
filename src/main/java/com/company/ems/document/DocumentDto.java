package com.company.ems.document;

import java.util.UUID;

public class DocumentDto {
	private UUID id;
    private String fileName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private UUID documentTypeId;
    private String documentTypeName;
    
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
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
	public UUID getDocumentTypeId() {
		return documentTypeId;
	}
	public void setDocumentTypeId(UUID documentTypeId) {
		this.documentTypeId = documentTypeId;
	}
	public String getDocumentTypeName() {
		return documentTypeName;
	}
	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
	}
    
}
