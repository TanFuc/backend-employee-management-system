package com.java.backend.mapper;

import com.java.backend.dto.UserDTO;
import com.java.backend.entity.UserCompanyEntity;
import com.java.backend.entity.UserEntity;
import com.java.backend.entity.UserRoleEntity;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static  UserDTO toDTO(UserEntity entity) {
        Set<String> roleNames = entity.getUserRoles() != null
            ? entity.getUserRoles().stream()
                .map(ur -> ur.getRole().getName())
                .collect(Collectors.toSet())
            : Set.of();

        Set<Long> companyIds = entity.getUserCompanies() != null
            ? entity.getUserCompanies().stream()
                .map(uc -> uc.getCompany().getId())
                .collect(Collectors.toSet())
            : Set.of();

        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setFullName(entity.getFullName());
        dto.setActive(entity.isActive());
        dto.setRoles(roleNames);
        dto.setCompanyIds(companyIds);

        return dto;
    }

    public static  UserEntity toEntity(UserDTO dto) {
        UserEntity user = new UserEntity();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setFullName(dto.getFullName());
        user.setActive(dto.isActive());
        return user;
    }
}
