package com.example.animeconsumer.consumer;

import com.example.animeconsumer.domain.Anime;
import com.example.animeconsumer.service.AnimeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @Autowired
    private AnimeService animeService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "anime-topic", groupId = "anime-group")
    public void consume(String message) {
        try {
            Anime anime = objectMapper.readValue(message, Anime.class);
            animeService.saveAnime(anime);
            System.out.println("Consumed message: " + anime);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error deserializing message: " + message);
        }
    }
}
