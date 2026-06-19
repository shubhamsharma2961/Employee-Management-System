package com.company.ems.config;

import com.company.ems.role.Role;
import com.company.ems.role.RoleRepository;
import com.company.ems.user.User;
import com.company.ems.user.UserRepository;
import com.company.ems.userrole.UserRole;
import com.company.ems.userrole.UserRoleRepository;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MenuSeeder menuSeeder;

    public DataSeeder(RoleRepository roleRepository, 
                      UserRepository userRepository, 
                      UserRoleRepository userRoleRepository, 
                      PasswordEncoder passwordEncoder,
                      MenuSeeder menuSeeder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.menuSeeder = menuSeeder;
    }

    @Override
    public void run(String... args) throws Exception {
        seedRoles();
        seedDefaultAdmin();
        menuSeeder.seedAllMenus(); 
    }

    private void seedRoles() {
        List<String> defaultRoles = List.of(
                "HR_ADMIN",
                "DEPARTMENT_HEAD",
                "STAFF"
        );
        for (String roleName : defaultRoles) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                role.setDescription("Default role for " + roleName);
                roleRepository.save(role);
            }
        }
    }

    private void seedDefaultAdmin() {
        String adminEmail = "admin@company.com";
        if (userRepository.findByEmailAndIsDeletedFalse(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("9876543210"));
            admin.setPasswordChanged(false);
            userRepository.save(admin);
            Role adminRole = roleRepository.findByName("HR_ADMIN")
                    .orElseThrow(() ->
                            new RuntimeException("HR_ADMIN role not found")
                    );
            UserRole userRole = new UserRole(admin, adminRole);
            userRoleRepository.save(userRole);
        }
    }
}
