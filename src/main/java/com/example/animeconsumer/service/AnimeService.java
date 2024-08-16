package com.example.animeconsumer.service;

import com.example.animeconsumer.adapters.out.mongodb.AnimeRepository;
import com.example.animeconsumer.domain.Anime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimeService {

    private final List<Anime> animeList = new ArrayList<>();

    private final AnimeRepository animeRepository;

    @Autowired
    public AnimeService(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }

    public List<Anime> getAnimes() {
        return animeList.stream()
                .sorted(Comparator.comparingDouble(Anime::getScore).reversed())
                .collect(Collectors.toList());
    }

    public Anime getAnimeById(String animeId) {
        return animeList.stream().filter(anime -> anime.getMalId().equals(animeId)).findFirst().orElse(null);
    }

    public void saveAnime(Anime anime) {
        animeList.add(anime);
        System.out.println("Anime added to list: " + anime.getTitle());
        removeDuplicates();
    }

    private void removeDuplicates() {
        List<Anime> uniqueAnimes = animeList.stream()
                .collect(Collectors.toMap(
                        Anime::getMalId,
                        anime -> anime,
                        (existing, replacement) -> existing.getOffset() > replacement.getOffset() ? existing : replacement
                ))
                .values()
                .stream()
                .collect(Collectors.toList());

        animeList.clear();
        animeList.addAll(uniqueAnimes);
    }

    public void clearAnimes() {
        animeList.clear();
        System.out.println("Anime list cleared");
    }

    public boolean limpaAnimes() {
        try {
            clearAnimes();
            return true;
        } catch (Exception e) {
            System.out.println("Erro no limpaAnimes()" + e);
            return false;
        }
    }
}