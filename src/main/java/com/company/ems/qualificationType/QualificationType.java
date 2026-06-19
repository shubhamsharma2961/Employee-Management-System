package com.company.ems.qualificationType;

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
@Table(name = "qualification_type", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class QualificationType extends BaseEntity{
	@Column(nullable = false, length = 100, columnDefinition = "VARCHAR(100)")
	private String name;
	@Column(length = 255, columnDefinition = "VARCHAR(255)")
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
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
