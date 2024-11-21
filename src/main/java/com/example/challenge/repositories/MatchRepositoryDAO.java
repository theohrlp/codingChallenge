package com.example.challenge.repositories;

import com.example.challenge.repositories.entities.MatchEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MatchRepositoryDAO extends CrudRepository<MatchEntity, UUID> {

}
