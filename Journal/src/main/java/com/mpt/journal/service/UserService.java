package com.mpt.journal.service;

import com.mpt.journal.model.RoleModel;
import com.mpt.journal.model.UserModel;
import com.mpt.journal.repository.RoleRepository;
import com.mpt.journal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserModel> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void createUser(UserModel user) {
        userRepository.save(user);
    }

    public void updateUser(Long id, UserModel user) {
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<UserModel> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<RoleModel> getAllRoles() {
        return roleRepository.findAll();
    }
}
