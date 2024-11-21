package com.example.challenge.repositories;

import com.example.challenge.repositories.entities.MatchEntity;

import java.util.Optional;
import java.util.UUID;

public interface MatchRepository {

    MatchEntity save(MatchEntity matchEntity);

    Optional<MatchEntity> findById(UUID id);

    void deleteById(UUID uuid);
}
