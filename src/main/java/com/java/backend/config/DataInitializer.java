package com.java.backend.config;

import com.java.backend.entity.*;
import com.java.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRepository userRepository;
    private final UserCompanyRepository userCompanyRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initCompanies();
        initPermissionsAndRoles();
        initUsers();
    }

    private void initCompanies() {
        if (companyRepository.count() == 0) {
            companyRepository.saveAll(List.of(
                    new CompanyEntity("Hệ thống", "Trụ sở chính", "Quản lý toàn bộ hệ thống", true),
                    new CompanyEntity("Công ty A", "Địa chỉ A", "Mô tả công ty A", true),
                    new CompanyEntity("Công ty B", "Địa chỉ B", "Mô tả công ty B", true)));
        }
    }

    private void initPermissionsAndRoles() {
        if (permissionRepository.count() == 0) {
            List<PermissionEntity> permissions = List.of(
                    new PermissionEntity("USER_READ", "USER_READ", "Quyền xem người dùng"),
                    new PermissionEntity("USER_CREATE", "USER_CREATE", "Quyền tạo người dùng"),
                    new PermissionEntity("USER_UPDATE", "USER_UPDATE", "Quyền cập nhật người dùng"),
                    new PermissionEntity("USER_DELETE", "USER_DELETE", "Quyền xóa người dùng"),
                    new PermissionEntity("EMPLOYEE_READ", "EMPLOYEE_READ", "Quyền xem nhân viên"),
                    new PermissionEntity("EMPLOYEE_CREATE", "EMPLOYEE_CREATE", "Quyền tạo nhân viên"),
                    new PermissionEntity("EMPLOYEE_UPDATE", "EMPLOYEE_UPDATE", "Quyền cập nhật nhân viên"),
                    new PermissionEntity("EMPLOYEE_DELETE", "EMPLOYEE_DELETE", "Quyền xóa nhân viên"),
                    new PermissionEntity("COMPANY_READ", "COMPANY_READ", "Quyền xem công ty"),
                    new PermissionEntity("COMPANY_CREATE", "COMPANY_CREATE", "Quyền tạo công ty"),
                    new PermissionEntity("COMPANY_UPDATE", "COMPANY_UPDATE", "Quyền cập nhật công ty"),
                    new PermissionEntity("COMPANY_DELETE", "COMPANY_DELETE", "Quyền xóa công ty"));
            permissionRepository.saveAll(permissions);
        }

        if (roleRepository.count() == 0) {

            RoleEntity superAdmin = new RoleEntity("SUPER_ADMIN", 9999);
            RoleEntity admin = new RoleEntity("ADMIN", 4);
            RoleEntity manager = new RoleEntity("MANAGER", 3);
            RoleEntity employee = new RoleEntity("EMPLOYEE", 2);
            RoleEntity user = new RoleEntity("USER", 1);
            roleRepository.saveAll(List.of(superAdmin, admin, manager, employee, user));

            createRolePermissions(superAdmin, List.of(
                    "USER_READ", "USER_CREATE", "USER_UPDATE", "USER_DELETE",
                    "EMPLOYEE_READ", "EMPLOYEE_CREATE", "EMPLOYEE_UPDATE", "EMPLOYEE_DELETE",
                    "COMPANY_READ", "COMPANY_CREATE", "COMPANY_UPDATE", "COMPANY_DELETE"));

            createRolePermissions(admin, List.of(
                    "USER_READ", "USER_CREATE", "USER_UPDATE",
                    "EMPLOYEE_READ", "EMPLOYEE_CREATE", "EMPLOYEE_UPDATE", "EMPLOYEE_DELETE"));

            createRolePermissions(manager, List.of(
                    "EMPLOYEE_READ", "EMPLOYEE_CREATE", "EMPLOYEE_UPDATE"));

            createRolePermissions(employee, List.of(
                    "EMPLOYEE_READ"));

            createRolePermissions(user, List.of(
                    "USER_READ"));
        }
    }

    private void createRolePermissions(RoleEntity role, List<String> permissionCodes) {
        List<RolePermissionEntity> mappings = new ArrayList<>();
        for (String code : permissionCodes) {
            PermissionEntity permission = findPermission(code);
            mappings.add(new RolePermissionEntity(role, permission));
        }
        rolePermissionRepository.saveAll(mappings);
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            CompanyEntity sysCompany = findCompany("Hệ thống");
            CompanyEntity companyA = findCompany("Công ty A");
            CompanyEntity companyB = findCompany("Công ty B");

            // SUPER_ADMIN
            UserEntity superAdmin = new UserEntity("superadmin", passwordEncoder.encode("123456"));
            superAdmin.setFullName("Super Admin");
            superAdmin.setActive(true);
            superAdmin.setIsSuperAdmin(true);
            userRepository.save(superAdmin);
            userRoleRepository.save(new UserRoleEntity(superAdmin, findRole("SUPER_ADMIN"), sysCompany));
            userCompanyRepository.save(new UserCompanyEntity(superAdmin, sysCompany));

            // USER1
            UserEntity user1 = new UserEntity("user1", passwordEncoder.encode("123456"));
            user1.setFullName("User One");
            user1.setActive(true);
            userRepository.save(user1);
            userRoleRepository.save(new UserRoleEntity(user1, findRole("USER"), companyA));
            userCompanyRepository.save(new UserCompanyEntity(user1, companyA));

            // USER2
            UserEntity user2 = new UserEntity("user2", passwordEncoder.encode("123456"));
            user2.setFullName("User Two");
            user2.setActive(true);
            userRepository.save(user2);
            userRoleRepository.save(new UserRoleEntity(user2, findRole("USER"), companyB));
            userCompanyRepository.save(new UserCompanyEntity(user2, companyB));

            // MANAGER1
            UserEntity manager1 = new UserEntity("manager1", passwordEncoder.encode("123456"));
            manager1.setFullName("Manager One");
            manager1.setActive(true);
            userRepository.save(manager1);
            userRoleRepository.save(new UserRoleEntity(manager1, findRole("MANAGER"), companyA));
            userCompanyRepository.save(new UserCompanyEntity(manager1, companyA));

            // ADMIN1
            UserEntity admin1 = new UserEntity("admin1", passwordEncoder.encode("123456"));
            admin1.setFullName("Admin One");
            admin1.setActive(true);
            userRepository.save(admin1);
            userRoleRepository.save(new UserRoleEntity(admin1, findRole("ADMIN"), companyB));
            userCompanyRepository.save(new UserCompanyEntity(admin1, companyB));
        }
    }

    private PermissionEntity findPermission(String code) {
        return permissionRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Permission not found: " + code));
    }

    private RoleEntity findRole(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role not found: " + name));
    }

    private CompanyEntity findCompany(String name) {
        return companyRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Company not found: " + name));
    }
}
