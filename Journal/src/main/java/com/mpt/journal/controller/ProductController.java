package com.mpt.journal.controller;

import com.mpt.journal.model.ArtistModel;
import com.mpt.journal.repository.AlbumRepository;
import com.mpt.journal.repository.ArtistRepository;
import com.mpt.journal.repository.GenreRepository;
import com.mpt.journal.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private TrackRepository trackRepository;

    @GetMapping
    public String showProducts(Model model) {

        model.addAttribute("artists", artistRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("albums", albumRepository.findAll());
        model.addAttribute("tracks", trackRepository.findAll());

        return "productControl";
    }
}
