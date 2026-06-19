package com.company.ems.security.jwt;

import com.company.ems.employee.Employee;
import com.company.ems.menu.MenuRepository;
import com.company.ems.user.User;
import com.company.ems.user.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    public CustomUserDetailsService(UserRepository userRepository, MenuRepository menuRepository) {
        this.userRepository = userRepository;
        this.menuRepository = menuRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        UUID companyId = null;
        UUID employeeId = null;
        UUID departmentId = null;

        if (user.getEmployee() != null) {
            Employee employee = user.getEmployee();
            employeeId = employee.getId();            
            if (employee.getCompany() != null) {
                companyId = employee.getCompany().getId();
            }
            if (employee.getDepartment() != null) {
                departmentId = employee.getDepartment().getId();
            }
        }
        
        List<String> permissionKeys = menuRepository.findPermissionKeysByUserId(user.getId());
        
        List<SimpleGrantedAuthority> authorities = permissionKeys.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        
        return new UserPrincipal(
        		user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getStatus(),
                companyId,
                employeeId,
                departmentId,
                authorities
        );
    }
}