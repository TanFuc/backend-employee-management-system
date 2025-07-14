package com.java.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.backend.entity.PermissionEntity;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {}
