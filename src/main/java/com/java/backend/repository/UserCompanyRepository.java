package com.java.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.backend.entity.CompanyEntity;
import com.java.backend.entity.UserCompanyEntity;
import com.java.backend.entity.UserEntity;

import java.util.Optional;

public interface UserCompanyRepository extends JpaRepository<UserCompanyEntity, Long> {

    List<UserCompanyEntity> findAllByUser(UserEntity user);

    boolean existsByUserAndCompanyId(UserEntity user, Long companyId);

    Optional<UserCompanyEntity> findByUserAndCompany(UserEntity user, CompanyEntity company);
}
