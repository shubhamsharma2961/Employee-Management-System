package com.company.ems.role;

import jakarta.validation.constraints.Size;

public class EditRoleDto {
	@Size(min = 2, max = 50, message = "Role name must be between 2 and 50 characters")
    private String name;

    @Size(max = 255, message = "Description too long")
    private String description;

    public EditRoleDto() {}

    public EditRoleDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

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
