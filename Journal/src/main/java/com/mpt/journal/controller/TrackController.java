package com.mpt.journal.controller;

import com.mpt.journal.model.TrackModel;
import com.mpt.journal.repository.TrackRepository;
import com.mpt.journal.repository.AlbumRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tracks")
public class TrackController {

    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private AlbumRepository albumRepository;

    @GetMapping
    public String listTracks(Model model) {
        model.addAttribute("tracks", trackRepository.findAll());
        return "productControl";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("track", new TrackModel());
        model.addAttribute("albums", albumRepository.findAll());
        return "trackCreate";
    }

    @PostMapping("/create")
    public String createTrack(@Valid @ModelAttribute("track") TrackModel trackModel, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("albums", albumRepository.findAll());
            return "trackCreate";
        }
        trackRepository.save(trackModel);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        TrackModel track = trackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid track Id:" + id));
        model.addAttribute("track", track);
        model.addAttribute("albums", albumRepository.findAll());
        return "trackEdit";
    }

    @PostMapping("/edit/{id}")
    public String updateTrack(@PathVariable("id") Long id, @Valid @ModelAttribute("track") TrackModel trackModel, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("albums", albumRepository.findAll());
            return "trackEdit";
        }

        TrackModel existingTrack = trackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid track Id:" + id));

        existingTrack.setTrackTitle(trackModel.getTrackTitle());
        existingTrack.setReleaseDate(trackModel.getReleaseDate());
        existingTrack.setDuration(trackModel.getDuration());
        existingTrack.setAlbum(trackModel.getAlbum());

        trackRepository.save(existingTrack);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteTrack(@PathVariable("id") Long id) {
        TrackModel track = trackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid track Id:" + id));
        trackRepository.delete(track);
        return "redirect:/products";
    }
}
