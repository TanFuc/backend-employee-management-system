package com.java.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.java.backend.dto.PermissionDTO;
import com.java.backend.dto.RoleDTO;
import com.java.backend.dto.UserDTO;
import com.java.backend.dto.request.UserRequest;
import com.java.backend.service.SuperAdminService;

import java.util.List;

@RestController
@RequestMapping("/api/superadmin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    public SuperAdminController(SuperAdminService superAdminService) {
        this.superAdminService = superAdminService;
    }

    // --- Users ---
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(superAdminService.getAllUsers());
    }
    
    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserRequest request) {
        return ResponseEntity.ok(superAdminService.createUser(request));
    }

    // --- Roles ---
    @GetMapping("/roles")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(superAdminService.getAllRoles());
    }

    @PostMapping("/roles")
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok(superAdminService.createRole(roleDTO));
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok(superAdminService.updateRole(id, roleDTO));
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        superAdminService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    // --- Permissions ---
    @GetMapping("/permissions")
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        return ResponseEntity.ok(superAdminService.getAllPermissions());
    }

    @PostMapping("/permissions")
    public ResponseEntity<PermissionDTO> createPermission(@RequestBody PermissionDTO dto) {
        return ResponseEntity.ok(superAdminService.createPermission(dto));
    }

    @PutMapping("/roles/{roleId}/permissions")
    public ResponseEntity<Void> updatePermissionsForRole(
            @PathVariable Long roleId,
            @RequestBody List<Long> permissionIds) {
        superAdminService.updateRolePermissions(roleId, permissionIds);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getSystemSummary() {
        return ResponseEntity.ok(superAdminService.getSystemSummary());
    }
}


