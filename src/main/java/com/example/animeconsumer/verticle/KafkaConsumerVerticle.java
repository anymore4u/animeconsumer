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

    private KafkaConsumer<String, String> consumer;
    private KafkaConsumer<String, String> controlConsumer;

    @PostConstruct
    public void init() {
        vertx.deployVerticle(this);
    }

    @Override
    public void start() {
        createConsumer();
        createControlConsumer();
    }

    private void createConsumer() {
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("group.id", "anime-group");
        config.put("auto.offset.reset", "earliest");
        config.put("enable.auto.commit", "false");

        consumer = KafkaConsumer.create(vertx, config);

        consumer.handler(record -> {
            System.out.println("Received record: " + record.value());
            processAndSaveAnime(record.value(), record.offset());
        });

        consumer.subscribe("anime-topic", ar -> {
            if (ar.succeeded()) {
                System.out.println("Subscribed to anime-topic successfully");
            } else {
                ar.cause().printStackTrace();
            }
        });
    }

    private void createControlConsumer() {
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("group.id", "control-group");
        config.put("auto.offset.reset", "earliest");
        config.put("enable.auto.commit", "false");

        controlConsumer = KafkaConsumer.create(vertx, config);

        controlConsumer.handler(record -> {
            System.out.println("Received control record: " + record.key() + " - " + record.value());
            if ("end-of-production".equals(record.key()) && "true".equalsIgnoreCase(record.value().replaceAll("\"", ""))) {
                System.out.println("End of production detected, repopulating anime list.");
                if (animeService.limpaAnimes()) consumeAndPopulateAnimeList();
            }
        });

        controlConsumer.subscribe("control-topic", ar -> {
            if (ar.succeeded()) {
                System.out.println("Subscribed to control-topic successfully");
            } else {
                ar.cause().printStackTrace();
            }
        });
    }

    private void processAndSaveAnime(String recordValue, long offset) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Anime anime = objectMapper.readValue(recordValue, Anime.class);
            anime.setOffset(offset);
            animeService.saveAnime(anime);
            System.out.println("Anime saved: " + anime.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error processing record: " + recordValue);
        }
    }

    public void consumeAndPopulateAnimeList() {
        System.out.println("Clearing anime list before repopulating from Kafka");
        System.out.println("Anime list cleared");

        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("group.id", "anime-group");
        config.put("auto.offset.reset", "earliest");
        config.put("enable.auto.commit", "false");

        KafkaConsumer<String, String> tempConsumer = KafkaConsumer.create(vertx, config);

        tempConsumer.handler(record -> {
            System.out.println("Received record: " + record.value());
            processAndSaveAnime(record.value(), record.offset());
        });

        tempConsumer.subscribe("anime-topic", ar -> {
            if (ar.succeeded()) {
                System.out.println("Subscribed to anime-topic successfully");
            } else {
                ar.cause().printStackTrace();
            }
        });
    }
}