package com.example.animeconsumer.controller;

import com.example.animeconsumer.service.AnimeService;
import com.example.animeconsumer.service.VoteService;
import com.example.animeconsumer.verticle.KafkaConsumerVerticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnimeController {

    @Autowired
    private AnimeService animeService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private KafkaConsumerVerticle kafkaConsumerVerticle;

    @GetMapping("/vote")
    public String getAnimes(Model model) {
        model.addAttribute("animes", animeService.getAnimes());
        return "vote";
    }

    @GetMapping("/vote-name")
    public String voteNamePage(@RequestParam String animeId, Model model) {
        model.addAttribute("animeId", animeId);
        return "vote-name";
    }

    @PostMapping("/submit-vote")
    public String submitVote(@RequestParam String animeId, @RequestParam String name, @RequestParam String userId) {
        boolean success = voteService.vote(animeId, userId, name);
        if (success) {
            return "vote-success";
        } else {
            return "vote-failure";
        }
    }
}
