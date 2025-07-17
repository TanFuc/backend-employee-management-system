package com.java.backend.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.java.backend.dto.PermissionDTO;
import com.java.backend.dto.RoleDTO;
import com.java.backend.dto.UserDTO;
import com.java.backend.dto.request.UserRequest;
import com.java.backend.entity.CompanyEntity;
import com.java.backend.entity.PermissionEntity;
import com.java.backend.entity.RoleEntity;
import com.java.backend.entity.RolePermissionEntity;
import com.java.backend.entity.UserCompanyEntity;
import com.java.backend.entity.UserEntity;
import com.java.backend.entity.UserRoleEntity;
import com.java.backend.mapper.RoleMapper;
import com.java.backend.mapper.UserMapper;
import com.java.backend.repository.CompanyRepository;
import com.java.backend.repository.PermissionRepository;
import com.java.backend.repository.RolePermissionRepository;
import com.java.backend.repository.RoleRepository;
import com.java.backend.repository.UserCompanyRepository;
import com.java.backend.repository.UserRepository;
import com.java.backend.repository.UserRoleRepository;
import com.java.backend.service.SuperAdminService;

import jakarta.transaction.Transactional;

@Service
@PreAuthorize("hasRole('SUPER_ADMIN')")
@Transactional
class SuperAdminServiceImpl implements SuperAdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;
    private final UserCompanyRepository userCompanyRepository;

    public SuperAdminServiceImpl(RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            RolePermissionRepository rolePermissionRepository,
            UserRepository userRepository,
            CompanyRepository companyRepository, PasswordEncoder passwordEncoder, UserRoleRepository userRoleRepository,
            UserMapper userMapper, UserCompanyRepository userCompanyRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
        this.userMapper = userMapper;
        this.userCompanyRepository = (UserCompanyRepository) userCompanyRepository;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(RoleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        RoleEntity role = new RoleEntity();
        role.setName(roleDTO.getName());
        role.setLevel(roleDTO.getLevel());
        return RoleMapper.toDTO(roleRepository.save(role));
    }

    @Override
    public RoleDTO updateRole(Long roleId, RoleDTO roleDTO) {
        RoleEntity role = roleRepository.findById(roleId).orElseThrow();
        role.setName(roleDTO.getName());
        role.setLevel(roleDTO.getLevel());
        return RoleMapper.toDTO(roleRepository.save(role));
    }

    @Override
    public void deleteRole(Long roleId) {
        roleRepository.deleteById(roleId);
    }

    @Override
    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(p -> new PermissionDTO(p.getId(), p.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void updatePermissionsForRole(Long roleId, List<Long> permissionIds) {
        RoleEntity role = roleRepository.findById(roleId).orElseThrow();

        rolePermissionRepository.deleteAll(role.getRolePermissions());

        List<RolePermissionEntity> newMappings = permissionIds.stream()
                .map(pid -> {
                    PermissionEntity permission = permissionRepository.findById(pid).orElseThrow();
                    RolePermissionEntity mapping = new RolePermissionEntity();
                    mapping.setRole(role);
                    mapping.setPermission(permission);
                    return mapping;
                })
                .collect(Collectors.toList());

        rolePermissionRepository.saveAll(newMappings);
    }

    @Override
    public PermissionDTO createPermission(PermissionDTO dto) {
        PermissionEntity permission = new PermissionEntity();
        permission.setName(dto.getName());
        PermissionEntity saved = permissionRepository.save(permission);
        return new PermissionDTO(saved.getId(), saved.getName());
    }

    @Override
    public void updateRolePermissions(Long roleId, List<Long> permissionIds) {
        RoleEntity role = roleRepository.findById(roleId).orElseThrow();
        rolePermissionRepository.deleteAll(role.getRolePermissions());

        List<RolePermissionEntity> newMappings = permissionIds.stream()
                .map(pid -> {
                    PermissionEntity permission = permissionRepository.findById(pid).orElseThrow();
                    RolePermissionEntity mapping = new RolePermissionEntity();
                    mapping.setRole(role);
                    mapping.setPermission(permission);
                    return mapping;
                }).collect(Collectors.toList());

        rolePermissionRepository.saveAll(newMappings);
    }

    @Override
    public Map<String, Long> getSystemSummary() {
        long totalUsers = userRepository.count();
        long totalRoles = roleRepository.count();
        long totalPermissions = permissionRepository.count();
        long totalCompanies = companyRepository.count();

        Map<String, Long> summary = new HashMap<>();
        summary.put("totalUsers", totalUsers);
        summary.put("totalRoles", totalRoles);
        summary.put("totalPermissions", totalPermissions);
        summary.put("totalCompanies", totalCompanies);

        return summary;
    }

    @Override
    @Transactional
    public UserDTO createUser(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setActive(true);
        user.setIsSuperAdmin(false);

        userRepository.save(user);

        Set<UserCompanyEntity> userCompanies = new HashSet<>();
        for (Long companyId : request.getCompanyIds()) {
            CompanyEntity company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            UserCompanyEntity userCompany = new UserCompanyEntity();
            userCompany.setUser(user);
            userCompany.setCompany(company);
            userCompanyRepository.save(userCompany);
            userCompanies.add(userCompany);
        }
        user.setUserCompanies(userCompanies);

        Set<UserRoleEntity> userRoles = new HashSet<>();
        for (Long companyId : request.getCompanyIds()) {
            CompanyEntity company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            for (Long roleId : request.getRoleIds()) {
                RoleEntity role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role not found"));
                UserRoleEntity userRole = new UserRoleEntity();
                userRole.setUser(user);
                userRole.setRole(role);
                userRole.setCompany(company);
                userRoleRepository.save(userRole);
                userRoles.add(userRole);
            }
        }
        user.setUserRoles(userRoles);

        return userMapper.toDTO(user);
    }

}
