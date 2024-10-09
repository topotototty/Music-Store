package com.mpt.journal.controller;

import com.mpt.journal.model.RoleModel;
import com.mpt.journal.model.UserModel;
import com.mpt.journal.repository.RoleRepository;
import com.mpt.journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminInitializer {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Добавляем PasswordEncoder

    @PostConstruct
    @Transactional
    public void createAdminUser() {
        if (userService.findByLogin("admin1").isEmpty()) {

            RoleModel adminRole = roleRepository.findByRoleName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        RoleModel newRole = new RoleModel("ROLE_ADMIN");
                        return roleRepository.save(newRole); // Сохраняем роль в базе данных
                    });

            String encodedPassword = passwordEncoder.encode("admin1!");
            UserModel adminUser = new UserModel("admin1", encodedPassword, "admin@gmail.com", adminRole);
            userService.createUser(adminUser);

            System.out.println("Администратор создан!");
        } else {
            System.out.println("Администратор уже существует.");
        }
    }
}
