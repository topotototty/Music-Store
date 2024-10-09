package com.mpt.journal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "genre", uniqueConstraints = {@UniqueConstraint(columnNames = "genre_name")})
public class GenreModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private Long genreId;

    @NotBlank(message = "Название жанра не может быть пустым")
    @Size(max = 50, message = "Название жанра должно быть не более 50 символов")
    @Column(name = "genre_name", nullable = false, length = 50, unique = true)
    private String genreName;
}
