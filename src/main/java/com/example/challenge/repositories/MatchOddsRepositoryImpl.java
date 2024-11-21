package com.example.challenge.repositories;

import com.example.challenge.repositories.entities.MatchOddsEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class MatchOddsRepositoryImpl implements MatchOddsRepository {

    MatchOddsRepositoryDAO repository;

    public MatchOddsRepositoryImpl(MatchOddsRepositoryDAO repository) {
        this.repository = repository;
    }

    @Override
    public MatchOddsEntity save(MatchOddsEntity matchOddsEntity) {
        return repository.save(matchOddsEntity);
    }

    @Override
    public Optional<MatchOddsEntity> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
