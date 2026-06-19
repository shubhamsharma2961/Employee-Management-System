package com.company.ems.experience;

import com.company.ems.employee.Employee;
import com.company.ems.employee.EmployeeRepository;
import com.company.ems.document.Document;
import com.company.ems.document.DocumentRepository;
import com.company.ems.common.ApprovalStatus;
import com.company.ems.security.jwt.UserPrincipal; 

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final EmployeeRepository employeeRepository;
    private final DocumentRepository documentRepository;
    private final ExperienceMapper experienceMapper;

    public ExperienceServiceImpl(ExperienceRepository experienceRepository,
                                 EmployeeRepository employeeRepository,
                                 DocumentRepository documentRepository,
                                 ExperienceMapper experienceMapper) {
        this.experienceRepository = experienceRepository;
        this.employeeRepository = employeeRepository;
        this.documentRepository = documentRepository;
        this.experienceMapper = experienceMapper;
    }

    @Override
    @Transactional
    public ExperienceDto createExperience(CreateExperienceDto dto) {
        UserPrincipal currentUser =
                (UserPrincipal) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();                        
        Employee employee = employeeRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Employee not linked with user"));
        Experience experience = experienceMapper.toEntity(dto);
        experience.setEmployee(employee);
        experience.setStatus(ApprovalStatus.PENDING);
        
        if (dto.getDocumentIds() != null && !dto.getDocumentIds().isEmpty()) {
            List<Document> targetDocuments = documentRepository.findAllById(dto.getDocumentIds());
            for (Document doc : targetDocuments) {
                doc.setExperience(experience);
            }
            documentRepository.saveAll(targetDocuments);
        }       
        Experience saved = experienceRepository.save(experience);
        return experienceMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ExperienceDto updateExperience(UUID id, EditExperienceDto dto) {
        Experience experience = experienceRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("Active experience entry data not found with ID: " + id));                   
        experienceMapper.updateEntityFromDto(dto, experience);
        experience.setStatus(ApprovalStatus.PENDING);       
        
        if (dto.getDocumentIds() != null && !dto.getDocumentIds().isEmpty()) {
            List<Document> freshDocuments = documentRepository.findAllById(dto.getDocumentIds());
            for (Document doc : freshDocuments) {
                if (!experience.getDocuments().contains(doc)) {
                    doc.setExperience(experience);
                    experience.getDocuments().add(doc);
                }
            }
        }      
        Experience saved = experienceRepository.save(experience);
        return experienceMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ExperienceDto getExperienceById(UUID id) {
        Experience experience = experienceRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("Active experience metadata record not found with ID: " + id));
        return experienceMapper.toDto(experience);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperienceDto> getExperiencesByEmployeeId(UUID employeeId) {
        List<Experience> experiences = experienceRepository.findAllByEmployeeIdActive(employeeId);
        return experienceMapper.toDtoList(experiences);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperienceDto> getMyExperiences() {
        UserPrincipal currentUser = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Employee employee = employeeRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Employee profile link context missing for user ID: " + currentUser.getId()));
        return getExperiencesByEmployeeId(employee.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperienceDto> getExperiencesByStatus(ApprovalStatus status) {
        List<Experience> queue = experienceRepository.findAllByStatusActive(status);
        return experienceMapper.toDtoList(queue);
    }

    @Override
    @Transactional
    public void updateStatus(UUID id, ApprovalStatus status, String remarks) {
        Experience experience = experienceRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("Active experience entry missing with ID: " + id));
        experience.setStatus(status);
        experience.setRemarks(remarks);
        experienceRepository.save(experience);
    }

    @Override
    @Transactional
    public void deleteExperience(UUID id) {
        Experience experience = experienceRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("Experience file entry not found with ID: " + id));
        experience.setDeleted(true); 
        experienceRepository.save(experience);
    }    
}