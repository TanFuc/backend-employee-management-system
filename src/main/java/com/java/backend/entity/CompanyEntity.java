package com.java.backend.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "companies")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    private String description;

    private boolean active = true;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<UserCompanyEntity> userCompanies;

    public CompanyEntity(String name, String address, String description, boolean active, UserEntity createdBy) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.active = active;
        this.createdBy = createdBy;
    }

    public CompanyEntity(String name, String address, String description, boolean active) {
        this(name, address, description, active, null);
    }

}
