package com.java.backend.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.backend.dto.request.CompanySelectionRequest;
import com.java.backend.dto.request.LoginRequest;
import com.java.backend.dto.request.SignupRequest;
import com.java.backend.dto.respone.JwtResponse;
import com.java.backend.entity.CompanyEntity;
import com.java.backend.entity.RoleEntity;
import com.java.backend.entity.UserCompanyEntity;
import com.java.backend.entity.UserEntity;
import com.java.backend.repository.RoleRepository;
import com.java.backend.repository.UserCompanyRepository;
import com.java.backend.repository.UserRepository;
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
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

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
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        boolean valid = userCompanyRepository.existsByUserAndCompanyId(user, request.getCompanyId());
        if (!valid) {
            return ResponseEntity.status(403).body("Bạn không thuộc công ty này");
        }

        String token = jwtUtils.generateTokenWithCompany(user.getUsername(), request.getCompanyId());
        return ResponseEntity.ok(new JwtResponse(token));
    }
}