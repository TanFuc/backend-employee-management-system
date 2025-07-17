package com.java.backend.service.impl;

import com.java.backend.dto.CompanyDTO;
import com.java.backend.dto.UserDTO;
import com.java.backend.entity.CompanyEntity;
import com.java.backend.entity.UserCompanyEntity;
import com.java.backend.mapper.UserMapper;
import com.java.backend.repository.CompanyRepository;
import com.java.backend.service.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CompanyDTO getCompanyById(Long id) {
        CompanyEntity company = companyRepository.findById(id).orElseThrow();
        return mapToDTO(company);
    }

    @Override
    public CompanyDTO createCompany(CompanyDTO dto) {
        CompanyEntity company = new CompanyEntity();
        company.setName(dto.getName());
        company.setAddress(dto.getAddress());
        company.setDescription(dto.getDescription());
        company.setActive(dto.isActive());

        company = companyRepository.save(company);
        return mapToDTO(company);
    }

    @Override
    public CompanyDTO updateCompany(Long id, CompanyDTO dto) {
        CompanyEntity company = companyRepository.findById(id).orElseThrow();
        company.setName(dto.getName());
        company.setAddress(dto.getAddress());
        company.setDescription(dto.getDescription());
        company.setActive(dto.isActive());

        company = companyRepository.save(company);
        return mapToDTO(company);
    }

    @Override
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    private CompanyDTO mapToDTO(CompanyEntity entity) {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAddress(entity.getAddress());
        dto.setDescription(entity.getDescription());
        dto.setActive(entity.isActive());

        if (entity.getUserCompanies() != null) {
            List<UserDTO> users = entity.getUserCompanies().stream()
                    .map(UserCompanyEntity::getUser)
                    .map(UserMapper::toDTO)
                    .collect(Collectors.toList());
            dto.setUsers(users);
        }

        return dto;
    }
}
