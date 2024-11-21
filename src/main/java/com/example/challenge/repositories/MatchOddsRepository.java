package com.example.challenge.repositories;

import com.example.challenge.repositories.entities.MatchOddsEntity;

import java.util.Optional;
import java.util.UUID;

public interface MatchOddsRepository {

    MatchOddsEntity save(MatchOddsEntity matchOddsEntity);

    Optional<MatchOddsEntity> findById(UUID id);

    void deleteById(UUID id);

}
