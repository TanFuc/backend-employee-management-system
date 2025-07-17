package com.java.backend.mapper;

import com.java.backend.dto.PermissionDTO;
import com.java.backend.entity.PermissionEntity;

public class PermissionMapper {

    public static PermissionDTO toDTO(PermissionEntity entity) {
        if (entity == null) return null;

        PermissionDTO dto = new PermissionDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public static PermissionEntity toEntity(PermissionDTO dto) {
        if (dto == null) return null;

        PermissionEntity entity = new PermissionEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }
}
