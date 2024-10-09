package com.mpt.journal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "album")
public class AlbumModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Long albumId;

    @NotBlank(message = "Название альбома не может быть пустым")
    @Size(max = 100, message = "Название альбома должно быть не более 100 символов")
    @Column(name = "album_title", nullable = false, length = 100)
    private String albumTitle;

    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    @Column(name = "release_date")
    private LocalDate releaseDate;

    @DecimalMin(value = "1.0", inclusive = false, message = "Цена должна быть больше 0")
    @Digits(integer = 10, fraction = 2, message = "Цена должна быть в формате DECIMAL(10,2)")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull(message = "Идентификатор исполнителя обязателен")
    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private ArtistModel artist;

    @NotNull(message = "Идентификатор жанра обязателен")
    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private GenreModel genre;
}
