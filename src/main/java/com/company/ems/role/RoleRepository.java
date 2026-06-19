package com.company.ems.role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
	Optional<Role> findByName(String name);
	
	@Query(value = """
	        SELECT p.name || ' > ' || c.name 
	        FROM role_hierarchy rh
	        JOIN roles p ON rh.parent_role_id = p.id
	        JOIN roles c ON rh.child_role_id = c.id
	        """, nativeQuery = true)
	    List<String> findAllHierarchyPairs();

}
