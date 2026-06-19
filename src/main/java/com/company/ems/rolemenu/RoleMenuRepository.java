package com.company.ems.rolemenu;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleMenuRepository extends JpaRepository<RoleMenu, UUID>{
	@Query("SELECT rm FROM RoleMenu rm JOIN FETCH rm.role JOIN FETCH rm.menu WHERE rm.role.id = :roleId")
	List<RoleMenu> findByRoleId(UUID roleId);

}
