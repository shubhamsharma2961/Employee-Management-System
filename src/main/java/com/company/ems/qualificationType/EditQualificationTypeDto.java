package com.company.ems.qualificationType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EditQualificationTypeDto {
	
	@NotBlank(message = "Qualification name is required")
    @Size(max = 100, message = "Qualification name must not exceed 100 characters")
    private String name;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
}
