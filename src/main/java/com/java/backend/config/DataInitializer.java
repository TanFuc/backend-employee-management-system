package com.java.backend.config;

import com.java.backend.entity.PermissionEntity;
import com.java.backend.entity.RoleEntity;
import com.java.backend.entity.RolePermissionEntity;
import com.java.backend.repository.PermissionRepository;
import com.java.backend.repository.RolePermissionRepository;
import com.java.backend.repository.RoleRepository;

import jakarta.transaction.Transactional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public DataInitializer(RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            RolePermissionRepository rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        createPermissionIfNotExist("READ");
        createPermissionIfNotExist("WRITE");
        createPermissionIfNotExist("UPDATE");
        createPermissionIfNotExist("DELETE");

        RoleEntity user = createRoleIfNotExist("USER", 1);
        RoleEntity staff = createRoleIfNotExist("STAFF", 2);
        RoleEntity manager = createRoleIfNotExist("MANAGER", 3);
        RoleEntity admin = createRoleIfNotExist("ADMIN", 4);

        assignPermissionsToRole(user, "READ");
        assignPermissionsToRole(staff, "READ", "WRITE");
        assignPermissionsToRole(manager, "READ", "WRITE", "UPDATE");
        assignPermissionsToRole(admin, "READ", "WRITE", "UPDATE", "DELETE");
    }

    private void createPermissionIfNotExist(String name) {
        permissionRepository.findByName(name).orElseGet(() -> {
            PermissionEntity p = new PermissionEntity();
            p.setName(name);
            return permissionRepository.save(p);
        });
    }

    private RoleEntity createRoleIfNotExist(String name, int level) {
        return roleRepository.findByName(name).orElseGet(() -> {
            RoleEntity role = new RoleEntity();
            role.setName(name);
            role.setLevel(level);
            return roleRepository.save(role);
        });
    }

    private void assignPermissionsToRole(RoleEntity role, String... permissionNames) {
        for (String permName : permissionNames) {
            PermissionEntity permission = permissionRepository.findByName(permName).orElseThrow();
            boolean exists = role.getRolePermissions().stream()
                    .anyMatch(rp -> rp.getPermission().getName().equals(permName));
            if (!exists) {
                RolePermissionEntity rp = new RolePermissionEntity();
                rp.setRole(role);
                rp.setPermission(permission);
                rolePermissionRepository.save(rp);
            }
        }
    }
}