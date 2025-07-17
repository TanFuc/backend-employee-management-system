
package com.java.backend.service.impl;

import com.java.backend.dto.RoleDTO;
import com.java.backend.entity.RoleEntity;
import com.java.backend.mapper.RoleMapper;
import com.java.backend.repository.RoleRepository;
import com.java.backend.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleDTO> getRolesByCompanyId(Long companyId) {
        List<RoleEntity> roles = roleRepository.findByCompanyId(companyId);
        return roles.stream()
                .map(RoleMapper::toDTO)
                .collect(Collectors.toList());
    }
}
