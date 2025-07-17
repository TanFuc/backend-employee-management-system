package com.java.backend.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.java.backend.dto.CompanyDTO;
import com.java.backend.dto.UserDTO;
import com.java.backend.entity.CompanyEntity;

public class CompanyMapper {

    public static CompanyDTO toDTO(CompanyEntity entity) {
        if (entity == null)
            return null;

        CompanyDTO dto = new CompanyDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAddress(entity.getAddress());
        dto.setDescription(entity.getDescription());
        dto.setActive(entity.isActive());

        if (entity.getUserCompanies() != null) {
            List<UserDTO> userDTOs = entity.getUserCompanies().stream()
                    .map(uc -> UserMapper.toDTO(uc.getUser()))
                    .collect(Collectors.toList());
            dto.setUsers(userDTOs);
        }

        return dto;
    }

    public static CompanyEntity toEntity(CompanyDTO dto) {
        if (dto == null)
            return null;

        CompanyEntity entity = new CompanyEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.isActive());

        return entity;
    }
}
