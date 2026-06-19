package com.company.ems.employee;

import java.util.UUID;

public class EmployeeSearchCriteria {
	private String search;
    private UUID departmentId;
    private UUID designationId;
    private UUID employeeTypeId;
   
    public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public UUID getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(UUID departmentId) {
		this.departmentId = departmentId;
	}

	public UUID getDesignationId() {
		return designationId;
	}

	public void setDesignationId(UUID designationId) {
		this.designationId = designationId;
	}

	public UUID getEmployeeTypeId() {
		return employeeTypeId;
	}

	public void setEmployeeTypeId(UUID employeeTypeId) {
		this.employeeTypeId = employeeTypeId;
	}

	public void enforceDepartmentRestriction(UUID mandatoryDeptId) {
        this.departmentId = mandatoryDeptId;
    }

}
