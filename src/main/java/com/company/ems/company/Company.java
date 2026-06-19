package com.company.ems.company;

import com.company.ems.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "company_details")
public class Company extends BaseEntity {

	@NotBlank(message = "Company name is required")
    @Size(max = 255)
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @NotBlank(message = "Company address is required")
    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;

    @NotBlank(message = "Company email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 150)
    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20)
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "logo_url", length = 512)
    private String logo; 

    @Size(max = 255)
    @Column(name = "website")
    private String website;
   
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
    
}