package com.mpt.journal.controller;

import com.mpt.journal.model.RoleModel;
import com.mpt.journal.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class RoleInitializer {

    @Autowired
    private RoleService roleService;

    @PostConstruct
    public void initRoles() {
        if (roleService.findByRoleName("ROLE_USER").isEmpty()) {
            roleService.createRole(new RoleModel("ROLE_USER"));
        }
        if (roleService.findByRoleName("ROLE_ADMIN").isEmpty()) {
            roleService.createRole(new RoleModel("ROLE_ADMIN"));
        }
        if (roleService.findByRoleName("ROLE_MANAGER").isEmpty()) {
            roleService.createRole(new RoleModel("ROLE_MANAGER"));
        }
    }
}
