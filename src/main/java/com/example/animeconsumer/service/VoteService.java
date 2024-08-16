package com.example.animeconsumer.service;

import com.example.animeconsumer.adapters.out.mongodb.VoteRepository;
import com.example.animeconsumer.domain.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public boolean vote(String animeId, String userId, String name) {
        // Verificar se o usuário já votou com o UUID
        List<Vote> existingVotes = voteRepository.findByUserId(userId);
        if (!existingVotes.isEmpty()) {
            // Usuário já votou
            return false;
        }

        // Salvar novo voto
        Vote vote = new Vote(animeId, userId, name, null);
        voteRepository.save(vote);
        return true;
    }

    public Map<String, Long> countVotesByAnime() {
        List<Vote> votes = voteRepository.findAll();
        return votes.stream()
                .collect(Collectors.groupingBy(Vote::getAnimeId, Collectors.counting()));
    }
}
