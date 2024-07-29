package com.example.animeconsumer.service;

import com.example.animeconsumer.domain.Anime;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimeService {

    private final List<Anime> animeList = new ArrayList<>();

    public List<Anime> getAnimes() {
        return animeList.stream()
                .filter(anime -> anime.getScore() != null && anime.getScore() > 0)
                .sorted(Comparator.comparingDouble(Anime::getScore).reversed())
                .collect(Collectors.toList());
    }

    public void saveAnime(Anime anime) {
        animeList.add(anime);
        System.out.println("Anime added to list: " + anime.getTitle());
    }
}
