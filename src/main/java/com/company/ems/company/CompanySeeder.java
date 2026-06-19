package com.company.ems.company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CompanySeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CompanySeeder.class);
    private final CompanyRepository companyRepository;

    public CompanySeeder(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedCompanyProfile();
    }

    private void seedCompanyProfile() {
        if (companyRepository.count() > 0) {
            logger.info("Company profile seeding skipped: Active or existing configuration detected.");
            return;
        }

        logger.info("Initializing system setup: Seeding default company configuration profile...");

        Company defaultCompany = new Company();
        defaultCompany.setCompanyName("Default Corporate Enterprise");
        defaultCompany.setAddress("123 Corporate Boulevard, Kathmandu, Nepal");
        defaultCompany.setEmail("info@corporate.com");
        defaultCompany.setPhoneNumber("9876543210");
        defaultCompany.setLogo("/assets/images/default-logo.png");
        defaultCompany.setWebsite("https://www.corporate.com");
        defaultCompany.setDeleted(false); 

        companyRepository.save(defaultCompany);
        logger.info("Company profile setup seeded successfully.");
    }
}
