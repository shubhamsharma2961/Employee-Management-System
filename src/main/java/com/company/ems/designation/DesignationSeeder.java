package com.company.ems.designation;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DesignationSeeder implements CommandLineRunner {

    private final DesignationRepository designationRepository;

    public DesignationSeeder(DesignationRepository designationRepository) {
        this.designationRepository = designationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedDesignations();
    }

    private void seedDesignations() {
        if (designationRepository.count() == 0) {

            Designation CEO = new Designation();
            CEO.setName("Chief Executive Officer");
            CEO.setCode("CEO");
            CEO.setDescription("Highest-ranking executive responsible for making major corporate decisions.");
            designationRepository.save(CEO);

            Designation CTO = new Designation();
            CTO.setName("Chief Technology Officer");
            CTO.setCode("CTO");
            CTO.setDescription("Head of technology infrastructure, research, and development.");
            designationRepository.save(CTO);

            Designation HRM = new Designation();
            HRM.setName("Human Resource Manager");
            HRM.setCode("HRM");
            HRM.setDescription("Responsible for managing recruitment, workplace policies, and talent retention.");
            designationRepository.save(HRM);

            Designation EM = new Designation();
            EM.setName("Engineering Manager");
            EM.setCode("EM");
            EM.setDescription("Oversees technical engineering teams, product roadmaps, and delivery metrics.");
            designationRepository.save(EM);

            Designation SSE = new Designation();
            SSE.setName("Senior Software Engineer");
            SSE.setCode("SSE");
            SSE.setDescription("Architects application design, drives development strategies, and mentors peers.");
            designationRepository.save(SSE);

            Designation SE = new Designation();
            SE.setName("Software Engineer");
            SE.setCode("SE");
            SE.setDescription("Builds, maintains, and optimizes software components and backend workflows.");
            designationRepository.save(SE);

            Designation QA = new Designation();
            QA.setName("Quality Assurance Engineer");
            QA.setCode("QA");
            QA.setDescription("Develops automated testing matrices to evaluate software stability and code security.");
            designationRepository.save(QA);

            Designation SA = new Designation();
            SA.setName("System Administrator");
            SA.setCode("SA");
            SA.setDescription("Maintains local security baselines, server runtimes, and system configurations.");
            designationRepository.save(SA);

            Designation ASSOCIATE = new Designation();
            ASSOCIATE.setName("Operations Associate");
            ASSOCIATE.setCode("OA");
            ASSOCIATE.setDescription("Handles day-to-day administrative workspace tasks and resource management.");
            designationRepository.save(ASSOCIATE);

            System.out.println(">> Master Data Seeding Complete: 9 foundational designations successfully synchronized.");
        } else {
            System.out.println(">> Master Data Seeding Skipped: Designation table already populated.");
        }
    }
}