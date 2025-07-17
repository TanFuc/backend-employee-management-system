package com.java.backend.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.java.backend.dto.RoleDTO;
import com.java.backend.entity.RoleEntity;

public class RoleMapper {

    public static RoleDTO toDTO(RoleEntity role) {
        if (role == null) return null;

        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setLevel(role.getLevel());

        Set<String> permissionNames = role.getRolePermissions().stream()
            .map(rp -> rp.getPermission().getName())
            .collect(Collectors.toSet());

        dto.setPermissions(permissionNames);

        return dto;
    }
}
