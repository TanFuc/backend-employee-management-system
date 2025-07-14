package com.java.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.backend.entity.CompanyEntity;
import com.java.backend.entity.UserCompanyEntity;
import com.java.backend.entity.UserEntity;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
	
    
	
}
