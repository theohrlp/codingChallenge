package com.example.challenge.services;

import com.example.challenge.repositories.MatchRepository;
import com.example.challenge.repositories.entities.MatchEntity;
import com.example.challenge.services.exceptions.ResourceNotFoundException;
import com.example.challenge.services.models.MatchModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class MatchService {
    private final ModelMapper modelMapper;
    private final MatchRepository matchRepository;

    public MatchService(ModelMapper modelMapper, MatchRepository matchRepository) {
        this.modelMapper = modelMapper;
        this.matchRepository = matchRepository;
    }

    @Transactional
    public MatchModel saveMatch(MatchModel matchModel) {
        MatchEntity matchEntity = modelMapper.map(matchModel, MatchEntity.class);
        matchEntity.setId(UUID.randomUUID());
        MatchEntity savedEntity = matchRepository.save(matchEntity);
        return modelMapper.map(savedEntity, MatchModel.class);
    }

    public Optional<MatchModel> getMatch(UUID idAsUuid) {
        return matchRepository.findById(idAsUuid)
                                     .map(matchEntity -> modelMapper.map(matchEntity, MatchModel.class));
    }

    @Transactional
    public boolean deleteMatch(UUID idAsString) {
        var matchEntityOptional = matchRepository.findById(idAsString);

        if (matchEntityOptional.isPresent()) {
            matchRepository.deleteById(idAsString);
            return true;
        }
        return false;
    }

    @Transactional
    public MatchModel updateMatch(MatchModel matchModel) {
        var matchEntityOptional = matchRepository.findById(matchModel.getId());
        if (matchEntityOptional.isPresent()) {
            MatchEntity updatedMathEntity = matchRepository.save(modelMapper.map(matchModel, MatchEntity.class));
            return modelMapper.map(updatedMathEntity, MatchModel.class);
        }
        else {
            throw new ResourceNotFoundException("Match not found with ID: {}", matchModel.getId());
        }
    }
}
