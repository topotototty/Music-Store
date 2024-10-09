package com.mpt.journal.controller;

import com.mpt.journal.model.ArtistModel;
import com.mpt.journal.repository.ArtistRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/artists")
public class ArtistController {

    @Autowired
    private ArtistRepository artistRepository;

    @GetMapping
    public String listArtists(Model model) {
        model.addAttribute("artists", artistRepository.findAll());
        return "productsControl";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("artist", new ArtistModel());
        return "artistCreate";
    }

    @PostMapping("/create")
    public String createArtist(@Valid @ModelAttribute("artist") ArtistModel artistModel, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "artistCreate";
        }
        // Проверяем уникальность имени артиста
        if (artistRepository.existsByArtistName(artistModel.getArtistName())) {
            result.rejectValue("artistName", "error.artist", "Артист с таким именем уже существует.");
            return "artistCreate";
        }
        artistRepository.save(artistModel);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        ArtistModel artist = artistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid artist Id:" + id));
        model.addAttribute("artist", artist);
        return "artistEdit";
    }

    @PostMapping("/edit/{id}")
    public String updateArtist(@PathVariable("id") Long id, @Valid @ModelAttribute("artist") ArtistModel artistModel, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "artistEdit";
        }

        // Проверяем уникальность имени артиста при редактировании
        ArtistModel existingArtist = artistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid artist Id:" + id));

        // Проверяем, если новое имя артиста уже существует у другого артиста
        if (!existingArtist.getArtistName().equals(artistModel.getArtistName()) &&
                artistRepository.existsByArtistName(artistModel.getArtistName())) {
            result.rejectValue("artistName", "error.artist", "Артист с таким именем уже существует.");
            return "artistEdit";
        }

        existingArtist.setArtistName(artistModel.getArtistName());
        artistRepository.save(existingArtist);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteArtist(@PathVariable("id") Long id) {
        ArtistModel artist = artistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid artist Id:" + id));
        artistRepository.delete(artist);
        return "redirect:/products";
    }
}
