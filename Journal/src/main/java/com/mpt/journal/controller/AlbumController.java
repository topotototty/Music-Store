package com.mpt.journal.controller;

import com.mpt.journal.model.AlbumModel;
import com.mpt.journal.repository.AlbumRepository;
import com.mpt.journal.repository.ArtistRepository;
import com.mpt.journal.repository.GenreRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private GenreRepository genreRepository;

    @GetMapping
    public String listAlbums(Model model) {
        model.addAttribute("albums", albumRepository.findAll());
        return "productsControl";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("album", new AlbumModel());
        model.addAttribute("artists", artistRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        return "albumCreate";
    }

    @PostMapping("/create")
    public String createAlbum(@Valid @ModelAttribute("album") AlbumModel albumModel, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("artists", artistRepository.findAll());
            model.addAttribute("genres", genreRepository.findAll());
            return "albumCreate";
        }
        if (albumRepository.existsByAlbumTitle(albumModel.getAlbumTitle())) {
            result.rejectValue("albumTitle", "error.album", "Альбом с таким названием уже существует.");
            model.addAttribute("artists", artistRepository.findAll());
            model.addAttribute("genres", genreRepository.findAll());
            return "albumCreate";
        }
        albumRepository.save(albumModel);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        AlbumModel album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid album Id:" + id));
        model.addAttribute("album", album);
        model.addAttribute("artists", artistRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        return "albumEdit";
    }

    @PostMapping("/edit/{id}")
    public String updateAlbum(@PathVariable("id") Long id, @Valid @ModelAttribute("album") AlbumModel albumModel, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("artists", artistRepository.findAll());
            model.addAttribute("genres", genreRepository.findAll());
            return "albumEdit";
        }

        AlbumModel existingAlbum = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid album Id:" + id));

        // Проверка уникальности названия альбома
        if (!existingAlbum.getAlbumTitle().equals(albumModel.getAlbumTitle()) &&
                albumRepository.existsByAlbumTitle(albumModel.getAlbumTitle())) {
            result.rejectValue("albumTitle", "error.album", "Альбом с таким названием уже существует.");
            model.addAttribute("artists", artistRepository.findAll());
            model.addAttribute("genres", genreRepository.findAll());
            return "albumEdit";
        }

        existingAlbum.setAlbumTitle(albumModel.getAlbumTitle());
        existingAlbum.setReleaseDate(albumModel.getReleaseDate());
        existingAlbum.setPrice(albumModel.getPrice());
        existingAlbum.setArtist(albumModel.getArtist());
        existingAlbum.setGenre(albumModel.getGenre());

        albumRepository.save(existingAlbum);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteAlbum(@PathVariable("id") Long id) {
        AlbumModel album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid album Id:" + id));
        albumRepository.delete(album);
        return "redirect:/products";
    }
}
