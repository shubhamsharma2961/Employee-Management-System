package com.company.ems.documentType;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class DocumentTypeSeeder implements CommandLineRunner {

    private final DocumentTypeRepository documentTypeRepository;

    public DocumentTypeSeeder(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedDocumentType("Citizenship", "Government-issued national identity documentation.");
        seedDocumentType("Passport", "International travel identification document.");
        seedDocumentType("Driving License", "Official document permitting operation of motor vehicles.");
        seedDocumentType("National ID Card", "Biometric national identity profile mapping tracking.");
    }

    private void seedDocumentType(String name, String description) {
        Optional<DocumentType> existing = documentTypeRepository.findByNameActiveAndTenantScope(name, null);
        
        if (existing.isEmpty()) {
            DocumentType type = new DocumentType();
            type.setName(name);
            type.setDescription(description);
            type.setDeleted(false);
            
            documentTypeRepository.save(type);
            System.out.println(">> Seeded Document Type: " + name);
        }
    }
}
