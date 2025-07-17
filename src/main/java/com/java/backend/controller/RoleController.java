package com.java.backend.controller;

import com.java.backend.dto.RoleDTO;
import com.java.backend.service.RoleService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // GET /api/roles?companyId=1
    @GetMapping
    public ResponseEntity<List<RoleDTO>> getRolesByCompany(@RequestParam Long companyId) {
        List<RoleDTO> roles = roleService.getRolesByCompanyId(companyId);
        return ResponseEntity.ok(roles);
    }
}
