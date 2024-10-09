package com.mpt.journal.service;

import com.mpt.journal.model.RoleModel;
import com.mpt.journal.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Optional<RoleModel> findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    public void createRole(RoleModel role) {
        roleRepository.save(role);
    }

    public List<RoleModel> getAllRoles() {
        return roleRepository.findAll();
    }
}
