package com.company.ems.company;

import java.util.UUID;

public class CompanyDto {
	private UUID id;
	private String companyName;
    private String address;
    private String email;
    private String phoneNumber;
    private String logo;
    private String website;
    
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
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
	public CompanyDto() {
		super();
	}
	public CompanyDto(String companyName, String address, String email, String phoneNumber, String logo,
			String website) {
		super();
		this.companyName = companyName;
		this.address = address;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.logo = logo;
		this.website = website;
	}
}
