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
@Table(name = "artist", uniqueConstraints = {@UniqueConstraint(columnNames = "artist_name")})
public class ArtistModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_id")
    private Long artistId;

    @NotBlank(message = "Имя артиста не может быть пустым")
    @Size(max = 100, message = "Имя артиста должно быть не более 100 символов")
    @Column(name = "artist_name", nullable = false, length = 100, unique = true)
    private String artistName;
}
