package com.mpt.journal.controller;

import com.mpt.journal.model.UserModel;
import com.mpt.journal.model.RoleModel;
import com.mpt.journal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping
    public String getAllUsersAndRoles(Model model) {
        List<UserModel> users = userService.getAllUsers();
        List<RoleModel> roles = userService.getAllRoles();
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "usersControl"; // Возвращаем шаблон для отображения таблиц пользователей и ролей
    }

    @GetMapping("/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new UserModel());
        model.addAttribute("roles", userService.getAllRoles());
        return "userCreate"; // Возвращаем шаблон для создания пользователя
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") @Valid UserModel user, BindingResult result, Model model) {
        // Проверяем, существует ли уже логин
        if (userService.findByLogin(user.getLogin()).isPresent()) {
            result.rejectValue("login", "error.user", "Этот логин уже занят. Пожалуйста, выберите другой.");
        }

        // Проверяем, существует ли уже email
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            result.rejectValue("email", "error.user", "Этот email уже используется. Пожалуйста, выберите другой.");
        }

        if (result.hasErrors()) {
            model.addAttribute("roles", userService.getAllRoles());
            return "userCreate"; // Возвращаем страницу создания пользователя с ошибками
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword); // Устанавливаем закодированный пароль

        // Здесь вы можете передать роль, если хотите, например, ROLE_USER
        RoleModel userRole = userService.getAllRoles().stream()
                .filter(role -> role.getRoleName().equals("ROLE_USER"))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Роль ROLE_USER не найдена"));

        user.setRole(userRole); // Устанавливаем роль пользователю

        // Сохраняем пользователя
        userService.createUser(user);
        return "redirect:/users"; // Перенаправляем обратно на список пользователей и ролей
    }


    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") @Valid UserModel user, BindingResult result, Model model) {
        UserModel existingUser = userService.getUserById(id).orElseThrow(() -> new IllegalArgumentException("Неверный ID пользователя: " + id));

        // Проверяем, существует ли уже логин
        if (!user.getLogin().equals(existingUser.getLogin()) && userService.findByLogin(user.getLogin()).isPresent()) {
            result.rejectValue("login", "error.user", "Этот логин уже занят. Пожалуйста, выберите другой.");
        }

        // Проверяем, существует ли уже email
        if (!user.getEmail().equals(existingUser.getEmail()) && userService.findByEmail(user.getEmail()).isPresent()) {
            result.rejectValue("email", "error.user", "Этот email уже используется. Пожалуйста, выберите другой.");
        }

        if (result.hasErrors()) {
            model.addAttribute("roles", userService.getAllRoles());
            return "userUpdate"; // Возвращаем страницу редактирования с ошибками
        }

        // Сохранение обновлённых данных
        boolean isUpdated = false;
        if (!user.getLogin().equals(existingUser.getLogin())) {
            existingUser.setLogin(user.getLogin());
            isUpdated = true;
        }
        if (!user.getEmail().equals(existingUser.getEmail())) {
            existingUser.setEmail(user.getEmail());
            isUpdated = true;
        }
        if (!user.getPassword().isEmpty()) { // Обновляем пароль только если он не пустой
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            isUpdated = true;
        }
        if (user.getRole() != null && !user.getRole().getRoleId().equals(existingUser.getRole().getRoleId())) {
            existingUser.setRole(user.getRole());
            isUpdated = true;
        }

        if (isUpdated) {
            userService.updateUser(id, existingUser);
        }

        return "redirect:/users"; // Перенаправляем обратно на список пользователей после обновления
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, Model model) {
        UserModel user = userService.getUserById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        model.addAttribute("roles", userService.getAllRoles());
        return "userUpdate"; // Возвращаем страницу редактирования пользователя
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users"; // Перенаправляем обратно на список пользователей после удаления
    }
}
