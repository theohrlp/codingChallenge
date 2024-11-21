package com.example.challenge.repositories;

import com.example.challenge.repositories.entities.MatchEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class MatchRepositoryImpl implements MatchRepository {

    MatchRepositoryDAO repository;

    public MatchRepositoryImpl(MatchRepositoryDAO repository) {this.repository = repository;}

    @Override
    public MatchEntity save(MatchEntity matchEntity) {
        return repository.save(matchEntity);
    }

    @Override
    public Optional<MatchEntity> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(UUID uuid) {
        repository.deleteById(uuid);
    }
}
