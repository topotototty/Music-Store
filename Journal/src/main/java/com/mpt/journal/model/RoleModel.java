package com.mpt.journal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @NotBlank(message = "Наименование роли не может быть пустым")
    @Size(max = 30, message = "Максимальная длина названия - 30 символов")
    @Column(nullable = false, length = 30)
    private String roleName;

    public RoleModel(String roleName) {
        this.roleName = roleName;
    }
}
