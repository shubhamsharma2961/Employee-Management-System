package com.company.ems.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.company.ems.department.Department;
import com.company.ems.department.DepartmentRepository;

@Component
public class DepartmentSeeder implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    
    public DepartmentSeeder(DepartmentRepository departmentRepository) {
    	this.departmentRepository = departmentRepository;
    }

    @Override
    public void run(String... args) {
        seedDepartments();
    }

    private void seedDepartments() {

        if (departmentRepository.count() > 0) {
            return;
        }

        Department hr = new Department();
        hr.setName("Human Resources");
        hr.setCode("HR");
        hr.setDescription("Handles employee relations and hiring");

        Department it = new Department();
        it.setName("Information Technology");
        it.setCode("IT");
        it.setDescription("Handles software and infrastructure");

        Department finance = new Department();
        finance.setName("Finance");
        finance.setCode("FIN");
        finance.setDescription("Handles accounting and budgeting");

        departmentRepository.save(hr);
        departmentRepository.save(it);
        departmentRepository.save(finance);
    }
  
}
