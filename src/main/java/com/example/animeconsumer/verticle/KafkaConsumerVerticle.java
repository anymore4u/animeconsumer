package com.example.animeconsumer.verticle;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.animeconsumer.domain.Anime;
import com.example.animeconsumer.service.AnimeService;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaConsumerVerticle extends AbstractVerticle {

    @Autowired
    private AnimeService animeService;

    @Autowired
    private Vertx vertx;

    @PostConstruct
    public void init() {
        vertx.deployVerticle(this);
    }

    @Override
    public void start() {
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("group.id", "anime-group");
        config.put("auto.offset.reset", "earliest");
        config.put("enable.auto.commit", "false");

        KafkaConsumer<String, String> consumer = KafkaConsumer.create(vertx, config);

        consumer.handler(record -> {
            System.out.println("Received record: " + record.value());
            processAndSaveAnime(record.value());
        });

        consumer.subscribe("anime-topic", ar -> {
            if (ar.succeeded()) {
                System.out.println("Subscribed to topic successfully");
            } else {
                ar.cause().printStackTrace();
            }
        });
    }

    private void processAndSaveAnime(String recordValue) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Anime anime = objectMapper.readValue(recordValue, Anime.class);
            animeService.saveAnime(anime);
            System.out.println("Anime saved: " + anime.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error processing record: " + recordValue);
        }
    }

}
