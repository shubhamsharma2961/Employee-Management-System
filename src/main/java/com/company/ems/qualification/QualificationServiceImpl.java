package com.company.ems.qualification;

import com.company.ems.employee.Employee;
import com.company.ems.employee.EmployeeRepository;
import com.company.ems.qualificationType.QualificationType;
import com.company.ems.qualificationType.QualificationTypeRepository;
import com.company.ems.document.Document;
import com.company.ems.document.DocumentRepository;
import com.company.ems.common.ApprovalStatus;
import com.company.ems.common.DataScope;
import com.company.ems.security.jwt.DataScopeService;
import com.company.ems.security.jwt.SecurityUtil;
import com.company.ems.security.jwt.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class QualificationServiceImpl implements QualificationService {

    private final QualificationRepository qualificationRepository;
    private final EmployeeRepository employeeRepository;
    private final QualificationTypeRepository qualificationTypeRepository;
    private final DocumentRepository documentRepository;
    private final QualificationMapper qualificationMapper;
    private final SecurityUtil securityUtil;
    private final DataScopeService dataScopeService;

    public QualificationServiceImpl(QualificationRepository qualificationRepository,
                                    EmployeeRepository employeeRepository,
                                    QualificationTypeRepository qualificationTypeRepository,
                                    DocumentRepository documentRepository,
                                    QualificationMapper qualificationMapper,
                                    SecurityUtil securityUtil,
                                    DataScopeService dataScopeService) {
        this.qualificationRepository = qualificationRepository;
        this.employeeRepository = employeeRepository;
        this.qualificationTypeRepository = qualificationTypeRepository;
        this.documentRepository = documentRepository;
        this.qualificationMapper = qualificationMapper;
        this.securityUtil = securityUtil;
        this.dataScopeService = dataScopeService;
    }

    @Override
    @Transactional
    public QualificationDto createQualification(CreateQualificationDto dto) {
    	UserPrincipal principal = securityUtil.getCurrentPrincipal();
        UUID tenantCompanyId = principal.getCompanyId();
        UUID employeeId = principal.getEmployeeId();
        if (employeeId == null) {
            throw new RuntimeException("Current user session context is not linked to any active employee record.");
        }
        Employee employee = employeeRepository.findByIdAndCompanyId(employeeId, tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Employee row not found or access denied across organization pools."));
        QualificationType type = qualificationTypeRepository.findByIdActiveAndTenantScope(dto.getQualificationTypeId(), tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Target qualification category lookup type not found or access denied."));
        Qualification qualification = qualificationMapper.toEntity(dto);
        qualification.setEmployee(employee);
        qualification.setCompany(employee.getCompany());
        qualification.setQualificationType(type);
        qualification.setStatus(ApprovalStatus.PENDING);

        if (dto.getDocumentIds() != null && !dto.getDocumentIds().isEmpty()) {
            List<Document> targetDocuments = documentRepository.findAllByIdAndCompanyId(dto.getDocumentIds(), tenantCompanyId);
            for (Document doc : targetDocuments) {
                doc.setQualification(qualification); 
            }
            qualification.getDocuments().addAll(targetDocuments);
        }

        Qualification saved = qualificationRepository.save(qualification);
        return qualificationMapper.toDto(saved);
//        UserPrincipal currentUser =
//                (UserPrincipal) SecurityContextHolder.getContext()
//                        .getAuthentication()
//                        .getPrincipal();
//        Employee employee = employeeRepository.findByUserId(currentUser.getId())
//                .orElseThrow(() ->
//                        new RuntimeException("Employee not linked with user"));
//        QualificationType type = qualificationTypeRepository.findById(dto.getQualificationTypeId())
//                .orElseThrow(() ->
//                        new RuntimeException("Qualification type not found"));
//        Qualification qualification = qualificationMapper.toEntity(dto);
//        qualification.setEmployee(employee);
//        qualification.setQualificationType(type);
//        qualification.setStatus(ApprovalStatus.PENDING);
//        if (dto.getDocumentIds() != null && !dto.getDocumentIds().isEmpty()) {
//            List<Document> targetDocuments = documentRepository.findAllById(dto.getDocumentIds());
//            for (Document doc : targetDocuments) {
//                doc.setQualification(qualification); 
//            }
//            qualification.getDocuments().addAll(targetDocuments);
//        }
//        Qualification saved = qualificationRepository.save(qualification);
//        return qualificationMapper.toDto(saved);
    }

    @Override
    @Transactional
    public QualificationDto updateQualification(UUID id, EditQualificationDto dto) {
    	UserPrincipal principal = securityUtil.getCurrentPrincipal();
        UUID tenantCompanyId = principal.getCompanyId();
        Qualification qualification = qualificationRepository.findByIdAndCompanyId(id, tenantCompanyId)
                .filter(q -> !q.isDeleted())
                .orElseThrow(() -> new RuntimeException("Active qualification entry data not found or access denied."));               
        UUID targetEmployeeId = qualification.getEmployee().getId();
        if (!targetEmployeeId.equals(principal.getEmployeeId())) {
            throw new AccessDeniedException("Access denied: You are not authorized to modify another user's qualification record.");
        }
        QualificationType type = qualificationTypeRepository.findByIdActiveAndTenantScope(dto.getQualificationTypeId(), tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Target Qualification configuration category not found."));
        qualificationMapper.updateEntityFromDto(dto, qualification);
        qualification.setQualificationType(type);
        qualification.setStatus(ApprovalStatus.PENDING);
        if (dto.getDocumentIds() != null && !dto.getDocumentIds().isEmpty()) {
            List<Document> freshDocuments = documentRepository.findAllByIdAndCompanyId(dto.getDocumentIds(), tenantCompanyId);
            for (Document doc : freshDocuments) {
                if (!qualification.getDocuments().contains(doc)) {
                    doc.setQualification(qualification);
                    qualification.getDocuments().add(doc);
                }
            }
        }
        Qualification updated = qualificationRepository.save(qualification);
        return qualificationMapper.toDto(updated);
//        Qualification qualification = qualificationRepository.findActiveById(id)
//                .orElseThrow(() -> new RuntimeException("Active qualification entry data not found with ID: " + id));               
//        QualificationType type = qualificationTypeRepository.findById(dto.getQualificationTypeId())
//                .orElseThrow(() -> new RuntimeException("Target Qualification configuration category not found"));
//        qualificationMapper.updateEntityFromDto(dto, qualification);
//        qualification.setQualificationType(type);
//        qualification.setStatus(ApprovalStatus.PENDING); 
//        if (dto.getDocumentIds() != null && !dto.getDocumentIds().isEmpty()) {
//            List<Document> freshDocuments = documentRepository.findAllById(dto.getDocumentIds());
//            for (Document doc : freshDocuments) {
//                if (!qualification.getDocuments().contains(doc)) {
//                	doc.setQualification(qualification);
//                    qualification.getDocuments().add(doc);
//                }
//            }
//        }
//        Qualification updated = qualificationRepository.save(qualification);
//        return qualificationMapper.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public QualificationDto getQualificationById(UUID id) {
        UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        Qualification qualification = qualificationRepository.findByIdAndCompanyId(id, tenantCompanyId)
                .filter(q -> !q.isDeleted())
                .orElseThrow(() -> new RuntimeException("Qualification record not found within your organization."));
        return qualificationMapper.toDto(qualification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QualificationDto> getQualificationsByEmployeeId(UUID employeeId) {
        UserPrincipal principal = securityUtil.getCurrentPrincipal();
        UUID tenantCompanyId = principal.getCompanyId();
        DataScope scope = dataScopeService.getCurrentUserScope();
        switch (scope) {
            case ALL -> {}
            case DEPARTMENT -> {
                Employee target = employeeRepository.findByIdAndCompanyId(employeeId, tenantCompanyId)
                        .orElseThrow(() -> new RuntimeException("Target profile data missing."));
                if (target.getDepartment() == null || !principal.getDepartmentId().equals(target.getDepartment().getId())) {
                    throw new AccessDeniedException("Access denied: Target profile falls outside your department visibility scope.");
                }
            }
            case SELF -> {
                if (!employeeId.equals(principal.getEmployeeId())) {
                    throw new AccessDeniedException("Access denied: Operational clearance restricted to your individual profile.");
                }
            }
            default -> throw new RuntimeException("Unsupported or corrupted data access scope structure.");
        }
        return qualificationRepository.findAllByEmployeeIdAndCompanyIdAndIsDeletedFalse(employeeId, tenantCompanyId)
                .stream().map(qualificationMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QualificationDto> getMyQualifications() {
        UserPrincipal principal = securityUtil.getCurrentPrincipal();
        return qualificationRepository.findAllByEmployeeIdAndCompanyIdAndIsDeletedFalse(principal.getEmployeeId(), principal.getCompanyId())
                .stream().map(qualificationMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QualificationDto> getQualificationsByStatus(ApprovalStatus status) {
        UserPrincipal principal = securityUtil.getCurrentPrincipal();
        UUID tenantCompanyId = principal.getCompanyId();
        DataScope scope = dataScopeService.getCurrentUserScope();      
        if (scope == DataScope.SELF) {
            throw new AccessDeniedException("Access denied: Standard user accounts are restricted from accessing dashboard queues.");
        }
        List<Qualification> queue = qualificationRepository.findAllByStatusAndCompanyIdAndIsDeletedFalse(status, tenantCompanyId);
        if (scope == DataScope.DEPARTMENT) {
            UUID managerDeptId = principal.getDepartmentId();
            queue = queue.stream()
                    .filter(q -> q.getEmployee().getDepartment() != null && q.getEmployee().getDepartment().getId().equals(managerDeptId))
                    .toList();
        }
        return qualificationMapper.toDtoList(queue);
    }

    @Override
    @Transactional
    public void updateStatus(UUID id, ApprovalStatus status, String remarks) {
        UserPrincipal principal = securityUtil.getCurrentPrincipal();
        UUID tenantCompanyId = principal.getCompanyId();
        DataScope scope = dataScopeService.getCurrentUserScope();
        if (scope == DataScope.SELF) {
            throw new AccessDeniedException("Access denied: Standard profiles cannot execute state transformations on approval workflows.");
        }
        Qualification qualification = qualificationRepository.findByIdAndCompanyId(id, tenantCompanyId)
                .filter(q -> !q.isDeleted())
                .orElseThrow(() -> new RuntimeException("Qualification entry missing or access denied."));
        if (scope == DataScope.DEPARTMENT) {
            UUID managerDeptId = principal.getDepartmentId();
            UUID empDeptId = qualification.getEmployee().getDepartment() != null ? qualification.getEmployee().getDepartment().getId() : null;
            if (!managerDeptId.equals(empDeptId)) {
                throw new AccessDeniedException("Access denied: Cannot process status revisions outside your assigned department branch.");
            }
        }
        qualification.setStatus(status);
        qualification.setRemarks(remarks);
        qualificationRepository.save(qualification);
    }

    @Override
    @Transactional
    public void deleteQualification(UUID id) {
        UserPrincipal principal = securityUtil.getCurrentPrincipal();
        UUID tenantCompanyId = principal.getCompanyId();
        Qualification qualification = qualificationRepository.findByIdAndCompanyId(id, tenantCompanyId)
                .filter(q -> !q.isDeleted())
                .orElseThrow(() -> new RuntimeException("Qualification record not found within your organization."));
        if (!qualification.getEmployee().getId().equals(principal.getEmployeeId())) {
            throw new AccessDeniedException("Access denied: You are not authorized to delete this record.");
        }
        qualification.setDeleted(true);
        qualificationRepository.save(qualification);
    }
    
    
}