
package com.company.ems.security.jwt;

import org.springframework.security.core.GrantedAuthority;

import com.company.ems.common.UserStatus;

import java.util.Collection;
import java.util.UUID;

public class UserPrincipal extends org.springframework.security.core.userdetails.User {

    private final UUID id;
    private final UserStatus status;
    private final UUID companyId;
    private final UUID employeeId;
    private final UUID departmentId;

    public UserPrincipal(
            UUID id,
            String email,
            String password,
            UserStatus status,
            UUID companyId,
            UUID employeeId,
            UUID departmentId,         
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(email, password, authorities);
        this.id = id;
        this.status = status;
        this.companyId = companyId;
        this.employeeId = employeeId;
        this.departmentId = departmentId;
    }
    
    public UUID getEmployeeId() {
		return employeeId;
	}

	public UUID getDepartmentId() {
		return departmentId;
	}

	public UUID getId() {
        return id;
    }

    public UserStatus getStatus() {
        return status;
    }
    
    public UUID getCompanyId() {
        return companyId;
    }
    
}