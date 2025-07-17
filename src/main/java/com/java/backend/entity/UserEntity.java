package com.java.backend.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "user")
    private Set<UserRoleEntity> userRoles;

    @OneToMany(mappedBy = "user")
    private Set<UserCompanyEntity> userCompanies;

    @Column(name = "is_super_admin")
    private Boolean isSuperAdmin = false;

    public Boolean getIsSuperAdmin() {
        return isSuperAdmin;
    }

    public void setIsSuperAdmin(Boolean isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }

    public UserEntity(String username, String password, String fullName, boolean active, boolean isSuperAdmin) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.active = active;
        this.isSuperAdmin = isSuperAdmin;
    }

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
