package com.company.ems.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>{
	@Query("SELECT u FROM User u WHERE u.email = :email AND u.isDeleted = false")
    Optional<User> findByEmailAndIsDeletedFalse(String email);   
    
	@Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
    boolean existsByEmail(String email);
    
	@Query("""
		    SELECT u FROM User u 
		    LEFT JOIN FETCH u.userRoles ur 
		    LEFT JOIN FETCH ur.role r 
		    LEFT JOIN FETCH u.employee e 
		    LEFT JOIN FETCH e.department d 
		    WHERE u.email = :email AND u.isDeleted = false
		""")
		Optional<User> findByEmailWithRoles(@Param("email") String email);
    
    List<User> findByEmailContainingIgnoreCase(String email);
    
    @Query("""
            SELECT u FROM User u 
            LEFT JOIN FETCH u.employee e 
            LEFT JOIN FETCH e.department d 
            WHERE u.email = :email AND u.isDeleted = false
        """)
        Optional<User> findByEmailWithDepartment(@Param("email") String email);

    @Query("""
    		SELECT u FROM User u
    		LEFT JOIN FETCH u.userRoles ur
    		LEFT JOIN FETCH ur.role
    		WHERE u.id = :id
    		""")
    		Optional<User> findByIdWithRoles(UUID id);
    }
