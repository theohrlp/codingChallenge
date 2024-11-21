package com.example.challenge.repositories;


import com.example.challenge.repositories.entities.MatchOddsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MatchOddsRepositoryDAO extends CrudRepository<MatchOddsEntity, UUID> {
}
