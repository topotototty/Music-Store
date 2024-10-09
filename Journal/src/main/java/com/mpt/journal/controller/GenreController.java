package com.mpt.journal.controller;

import com.mpt.journal.model.GenreModel;
import com.mpt.journal.repository.GenreRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    private GenreRepository genreRepository;

    @GetMapping
    public String listGenres(Model model) {
        model.addAttribute("genres", genreRepository.findAll());
        return "productsControl";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("genre", new GenreModel());
        return "genreCreate";
    }

    @PostMapping("/create")
    public String createGenre(@Valid @ModelAttribute("genre") GenreModel genreModel, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "genreCreate";
        }
        // Проверяем уникальность имени жанра
        if (genreRepository.existsByGenreName(genreModel.getGenreName())) {
            result.rejectValue("genreName", "error.genre", "Жанр с таким названием уже существует.");
            return "genreCreate";
        }
        genreRepository.save(genreModel);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        GenreModel genre = genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid genre Id:" + id));
        model.addAttribute("genre", genre);
        return "genreEdit";
    }

    @PostMapping("/edit/{id}")
    public String updateGenre(@PathVariable("id") Long id, @Valid @ModelAttribute("genre") GenreModel genreModel, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "genreEdit";
        }

        // Проверяем уникальность имени жанра при редактировании
        GenreModel existingGenre = genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid genre Id:" + id));

        if (!existingGenre.getGenreName().equals(genreModel.getGenreName()) &&
                genreRepository.existsByGenreName(genreModel.getGenreName())) {
            result.rejectValue("genreName", "error.genre", "Жанр с таким названием уже существует.");
            return "genreEdit";
        }

        existingGenre.setGenreName(genreModel.getGenreName());
        genreRepository.save(existingGenre);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteGenre(@PathVariable("id") Long id) {
        GenreModel genre = genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid genre Id:" + id));
        genreRepository.delete(genre);
        return "redirect:/products";
    }
}
