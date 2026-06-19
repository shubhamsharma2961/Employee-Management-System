package com.company.ems.menu;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, UUID>{
	List<Menu> findAllByIsDeletedFalseOrderBySortOrderAsc();
	
	Optional<Menu> findByPermissionKey(String permissionKey);
	
	@Query(value = "WITH RECURSIVE RoleTree AS ( " +
            "    SELECT r.id FROM roles r " +
            "    INNER JOIN user_roles ur ON ur.role_id = r.id " +
            "    WHERE ur.user_id = :userId " +
            "    UNION " +
            "    SELECT child.id FROM roles child " +
            "    INNER JOIN role_hierarchy rh ON rh.child_role_id = child.id " +
            "    INNER JOIN RoleTree parent ON rh.parent_role_id = parent.id " +
            ") " +
            "SELECT DISTINCT m.permission_key FROM role_menus rm " +
            "INNER JOIN menus m ON rm.menu_id = m.id " +
            "WHERE rm.role_id IN (SELECT id FROM RoleTree) " +
            "AND m.permission_key IS NOT NULL", 
    nativeQuery = true)
    List<String> findPermissionKeysByUserId(@Param("userId") UUID userId);
	
	Optional<Menu> findById(UUID parentId);
	
	Optional<Menu> findByName(String name);

}
