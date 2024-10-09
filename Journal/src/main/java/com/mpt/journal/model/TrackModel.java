package com.mpt.journal.model;

import com.mpt.journal.model.AlbumModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "track")
public class TrackModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id")
    private Long trackId;

    @NotBlank(message = "Название трека не может быть пустым")
    @Size(max = 100, message = "Название трека должно быть не более 100 символов")
    @Column(name = "track_title", nullable = false, length = 100)
    private String trackTitle;

    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    @Column(name = "release_date")
    private LocalDate releaseDate;

    @NotNull(message = "Длительность трека обязательна")
    @Column(name = "duration", nullable = false)
    private LocalTime duration;

    @NotNull(message = "Идентификатор альбома обязателен")
    @ManyToOne
    @JoinColumn(name = "album_id", nullable = false)
    private AlbumModel album;
}
