package com.example.animeconsumer.controller;

import com.example.animeconsumer.service.AnimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnimeController {

    @Autowired
    private AnimeService animeService;

    @GetMapping("/vote")
    public String getAnimes(Model model) {
        model.addAttribute("animes", animeService.getAnimes());
        return "vote";
    }
}
