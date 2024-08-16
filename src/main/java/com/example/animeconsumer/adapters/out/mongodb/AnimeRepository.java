package com.example.animeconsumer.adapters.out.mongodb;

import com.example.animeconsumer.domain.Anime;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnimeRepository extends MongoRepository<Anime, String> {
    Anime findByMalId(String malId);
}
