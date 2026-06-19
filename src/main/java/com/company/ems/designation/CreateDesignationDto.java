package com.company.ems.designation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateDesignationDto {
	@NotBlank(message = "Designation name is required.")
    @Size(min = 2, max = 100, message = "Designation name must be between 2 and 100 characters.")
    private String name;

    @NotBlank(message = "Designation code is required.")
    @Size(min = 2, max = 10, message = "Designation code must be between 2 and 10 characters.")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Designation code must be alphanumeric and uppercase.")
    private String code;

    @Size(max = 255, message = "Description cannot exceed 255 characters.")
    private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
}
