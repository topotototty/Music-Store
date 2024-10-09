package com.mpt.journal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 6, max = 30, message = "Логин должен содержать от 6 до 30 символов")
    @Column(nullable = false, unique = true)
    private String login;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*]).+$", message = "Пароль должен содержать хотя бы одну цифру или специальный символ")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Некорректный формат электронной почты")
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @ManyToOne
    @JoinColumn(name = "role", nullable = false)
    private RoleModel role;

    public UserModel(String login, String password, String email, RoleModel role) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}
