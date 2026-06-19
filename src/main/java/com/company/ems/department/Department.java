package com.company.ems.department;

import com.company.ems.common.BaseEntity;
import com.company.ems.company.Company;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "departments", uniqueConstraints = {@UniqueConstraint(columnNames = "name"), @UniqueConstraint(columnNames = "code")})
public class Department extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    
    @Column(nullable = false, unique = true, length = 10)
    private String code;
    
    @Column(length = 255)
    private String description;

    public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Department() {}
    
    public Department(String name) { 
    	this.name = name; 
   	}

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
