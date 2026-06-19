package com.company.ems.qualificationType;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class QualificationTypeSeeder implements CommandLineRunner {

    private final QualificationTypeRepository qualificationTypeRepository;

    public QualificationTypeSeeder(QualificationTypeRepository qualificationTypeRepository) {
        this.qualificationTypeRepository = qualificationTypeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedQualification("Bachelor's", "Undergraduate academic degree program.");
        seedQualification("Master's", "Postgraduate advanced academic degree program.");
        seedQualification("PhD", "Highest university academic degree (Doctor of Philosophy).");
        seedQualification("Diploma", "Technical or vocational qualification certificate.");
        seedQualification("Certification", "Professional industry-standard validation credentials.");
    }

    private void seedQualification(String name, String description) {
        Optional<QualificationType> existing = qualificationTypeRepository.findByNameActiveAndTenantScope(name, null);
        
        if (existing.isEmpty()) {
            QualificationType type = new QualificationType();
            type.setName(name);
            type.setDescription(description);
            type.setDeleted(false);
            
            qualificationTypeRepository.save(type);
            System.out.println(">> Seeded Qualification Type: " + name);
        }
    }
}