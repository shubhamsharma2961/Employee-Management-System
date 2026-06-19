package com.company.ems.employeeType;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class EmployeeTypeSeeder implements CommandLineRunner {

    private final EmployeeTypeRepository employeeTypeRepository;

    public EmployeeTypeSeeder(EmployeeTypeRepository employeeTypeRepository) {
        this.employeeTypeRepository = employeeTypeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedType("Permanent", "PERM", "Full-time employees with standard corporate benefits.");
        seedType("Contract", "CONT", "Fixed-term or project-based agreement workforce.");
        seedType("Intern", "INT", "Short-term learning or probationary tracking roles.");
    }

    private void seedType(String name, String code, String description) {
        Optional<EmployeeType> existingType = employeeTypeRepository.findByCodeActiveAndTenantScope(code, null);
        
        if (existingType.isEmpty()) {
            EmployeeType newType = new EmployeeType();
            newType.setName(name);
            newType.setCode(code);
            newType.setDescription(description);
            newType.setDeleted(false);
            
            employeeTypeRepository.save(newType);
            System.out.println(">> Seeded Employee Type: " + code);
        }
    }
}