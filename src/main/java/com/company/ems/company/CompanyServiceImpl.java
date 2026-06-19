package com.company.ems.company;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyDto getCompanyById(UUID id) {
        Company company = companyRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("Company profile not found with ID: " + id));
        return companyMapper.toDto(company);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyDto> getAllCompanyRecords() {
        List<Company> companies = companyRepository.findAll().stream()
                .filter(c -> !c.isDeleted())
                .toList();
        return companyMapper.toDtoList(companies);
    }

    @Override
    @Transactional
    public CompanyDto createCompany(CreateCompanyDto createCompanyDto) {
        Company company = companyMapper.toEntity(createCompanyDto);
        Company savedCompany = companyRepository.save(company);
        return companyMapper.toDto(savedCompany);
    }

    @Override
    @Transactional
    public CompanyDto updateCompany(UUID id, EditCompanyDto editCompanyDto) {
        Company company = companyRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("Active company profile not found to update with ID: " + id));

        companyMapper.updateEntityFromDto(editCompanyDto, company);
        Company updatedCompany = companyRepository.save(company);
        return companyMapper.toDto(updatedCompany);
    }

    @Override
    @Transactional
    public void deleteCompany(UUID id) {
        Company company = companyRepository.findActiveById(id)
               .orElseThrow(() -> new RuntimeException("Active company profile not found to delete with ID: " + id));     
        company.setDeleted(true); 
        companyRepository.save(company);
    }
}