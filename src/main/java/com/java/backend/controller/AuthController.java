package com.java.backend.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.java.backend.dto.request.CompanySelectionRequest;
import com.java.backend.dto.request.LoginRequest;
import com.java.backend.entity.*;
import com.java.backend.repository.*;
import com.java.backend.utils.JwtUtils;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private UserCompanyRepository userCompanyRepository;

        @Autowired
        private UserRoleRepository userRoleRepository;

        @Autowired
        private RolePermissionRepository rolePermissionRepository;

        @Autowired
        private PermissionRepository permissionRepository;

        @Autowired
        private JwtUtils jwtUtils;

        @Autowired
        private CompanyRepository companyRepository;

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                                                loginRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                UserEntity user = userRepository.findByUsername(loginRequest.getUsername())
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                List<CompanyEntity> companies = userCompanyRepository.findAllByUser(user)
                                .stream()
                                .map(UserCompanyEntity::getCompany)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(companies);
        }

        @PostMapping("/select-company")
        public ResponseEntity<?> selectCompany(@RequestBody CompanySelectionRequest request) {
                UserEntity user = userRepository.findWithRolesAndPermissionsByUsername(request.getUsername())
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                Long companyId = request.getCompanyId();

                boolean valid = userCompanyRepository.existsByUserAndCompanyId(user, companyId);
                if (!valid) {
                        return ResponseEntity.status(403).body("Bạn không thuộc công ty này");
                }

                CompanyEntity company = companyRepository.findById(companyId)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy công ty"));

                List<String> roleCodes = user.getUserRoles().stream()
                                .filter(ur -> ur.getCompany() != null && ur.getCompany().getId().equals(companyId))
                                .map(ur -> ur.getRole() != null ? ur.getRole().getName() : "Unknown")
                                .collect(Collectors.toList());

                Set<String> permissionCodes = user.getUserRoles().stream()
                                .filter(ur -> ur.getCompany() != null && ur.getCompany().getId().equals(companyId))
                                .filter(ur -> ur.getRole() != null && ur.getRole().getRolePermissions() != null)
                                .flatMap(ur -> ur.getRole().getRolePermissions().stream())
                                .filter(rp -> rp.getPermission() != null && rp.getPermission().getCode() != null)
                                .map(rp -> rp.getPermission().getCode())
                                .collect(Collectors.toSet());

                String token = jwtUtils.generateTokenWithClaims(
                                user.getUsername(),
                                companyId,
                                roleCodes,
                                permissionCodes);

                return ResponseEntity.ok(Map.of(
                                "token", token,
                                "username", user.getUsername(),
                                "companyId", companyId,
                                "companyName", company.getName(),
                                "roles", roleCodes,
                                "permissions", permissionCodes));
        }

}