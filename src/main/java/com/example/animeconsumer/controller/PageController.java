package com.example.animeconsumer.controller;

import com.example.animeconsumer.domain.Anime;
import com.example.animeconsumer.service.AnimeService;
import com.example.animeconsumer.service.VoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/animes")
public class PageController {

    private final AnimeService animeService;
    private final VoteService voteService;

    public PageController(AnimeService animeService, VoteService voteService) {
        this.animeService = animeService;
        this.voteService = voteService;
    }

    @GetMapping
    public List<Anime> getAllAnimes() {
        return animeService.getAnimes();
    }

    @GetMapping("/votes")
    public Map<String, Long> getAnimeVotes() {
        return voteService.countVotesByAnime();
    }

    @GetMapping("/update")
    public String updateAnimes() {
        animeService.getAnimes();
        return "Anime data updated successfully.";
    }

/*    @GetMapping("/trigger-update")
    public String triggerUpdateAnimes() {
        animeService.triggerUpdateAnimeData();
        return "Scheduled anime data update triggered successfully.";
    }*/
}
