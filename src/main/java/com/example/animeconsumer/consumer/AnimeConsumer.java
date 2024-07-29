package com.example.animeconsumer.consumer;

import com.example.animeconsumer.domain.Anime;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AnimeConsumer {

    @KafkaListener(topics = "anime-topic", groupId = "anime-group")
    public void consume(Anime anime) {
        System.out.println("Consumed: " + anime);
    }
}

