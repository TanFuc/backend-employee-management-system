package com.java.backend.service;

import java.util.List;
import java.util.Map;

import com.java.backend.dto.PermissionDTO;
import com.java.backend.dto.RoleDTO;
import com.java.backend.dto.UserDTO;
import com.java.backend.dto.request.UserRequest;

public interface SuperAdminService {
    List<RoleDTO> getAllRoles();
    List<UserDTO> getAllUsers();
    RoleDTO createRole(RoleDTO roleDTO);

    RoleDTO updateRole(Long roleId, RoleDTO roleDTO);

    void deleteRole(Long roleId);

    List<PermissionDTO> getAllPermissions();

    void updatePermissionsForRole(Long roleId, List<Long> permissionIds);
    
    PermissionDTO createPermission(PermissionDTO dto);   
    
    void updateRolePermissions(Long roleId, List<Long> permissionIds); 
    
    Map<String, Long> getSystemSummary();
    
    UserDTO createUser(UserRequest request);

}