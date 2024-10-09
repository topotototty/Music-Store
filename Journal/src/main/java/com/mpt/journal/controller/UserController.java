package com.mpt.journal.controller;

import com.mpt.journal.model.UserModel;
import com.mpt.journal.service.RoleService;
import com.mpt.journal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/userControl")
    public String userControlPage(Model model) {
        model.addAttribute("user", new UserModel());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRoles());
        return "userControl";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute UserModel user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("roles", roleService.getAllRoles());
            return "userControl";
        }
        userService.createUser(user);
        return "redirect:/users/userControl";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @Valid @ModelAttribute("user") UserModel user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", new UserModel());
            model.addAttribute("roles", userService.getAllRoles());  // Не забудь роли!
            return "userControl";
        }

        userService.updateUser(id, user);
        return "redirect:/users/userControl";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        Optional<UserModel> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            model.addAttribute("roles", userService.getAllRoles());
            return "userControl";
        }
        return "redirect:/users/userControl";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users/userControl";
    }

    @GetMapping("/getUser/{id}")
    @ResponseBody
    public Optional<UserModel> getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
