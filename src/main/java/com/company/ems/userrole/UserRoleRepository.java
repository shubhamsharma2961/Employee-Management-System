package com.company.ems.userrole;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID>{
	List<UserRole> findByUserId(UUID userId);	
}
