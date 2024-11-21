package com.example.challenge.services;

import com.example.challenge.repositories.MatchOddsRepository;
import com.example.challenge.repositories.MatchRepository;
import com.example.challenge.repositories.entities.MatchEntity;
import com.example.challenge.repositories.entities.MatchOddsEntity;
import com.example.challenge.services.exceptions.InvalidRequestException;
import com.example.challenge.services.exceptions.ResourceNotFoundException;
import com.example.challenge.services.models.MatchOddsModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class MatchOddsService {

    private final ModelMapper modelMapper;
    private final MatchOddsRepository matchOddsRepository;
    private final MatchRepository matchRepository;

    public MatchOddsService(ModelMapper modelMapper, MatchOddsRepository matchOddsRepository, MatchRepository matchRepository) {
        this.modelMapper = modelMapper;
        this.matchOddsRepository = matchOddsRepository;
        this.matchRepository = matchRepository;
    }


    @Transactional
    public MatchOddsModel saveMatchOdd(MatchOddsModel matchOddsModel) {
        // Validate the odds field
        if (matchOddsModel.getOdd().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("Odds must be greater than zero.");
        }

        // Ensure the match relationship is set
        MatchEntity matchEntity = matchRepository.findById(matchOddsModel.getMatchId())
                                                 .orElseThrow(() -> new ResourceNotFoundException("Match not found with ID: " + matchOddsModel.getMatchId()));

        // Map the model to the entity
        MatchOddsEntity matchOddsEntity = modelMapper.map(matchOddsModel, MatchOddsEntity.class);
        matchOddsEntity.setId(UUID.randomUUID());
        matchOddsEntity.setMatch(matchEntity);

        // Save the entity and return the result
        MatchOddsEntity savedMatchOddsEntity = matchOddsRepository.save(matchOddsEntity);
        return modelMapper.map(savedMatchOddsEntity, MatchOddsModel.class);
    }


    public MatchOddsModel getMatchOdd(UUID matchIdAsUuid, UUID matchOddIdAsUuid) {
        MatchOddsEntity matchOddsEntity = matchOddsRepository.findById(matchOddIdAsUuid)
                                                             .filter(odds -> odds.getMatch().getId().equals(matchIdAsUuid))
                                                             .orElseThrow(() -> new ResourceNotFoundException("MatchOdd not found or does not belong to the specified match"));

        return modelMapper.map(matchOddsEntity, MatchOddsModel.class);
    }

    @Transactional
    public boolean deleteMatchOdd(UUID matchIdAsUuid, UUID matchOddIdAsUuid) {
        // Fetch the MatchOddsEntity to validate the matchId relationship
        MatchOddsEntity matchOddsEntity = matchOddsRepository.findById(matchOddIdAsUuid)
                                                             .filter(odds -> odds.getMatch().getId().equals(matchIdAsUuid))
                                                             .orElseThrow(() -> new ResourceNotFoundException("MatchOdd with ID: " + matchOddIdAsUuid + " not found or does not belong to match with ID: " + matchIdAsUuid));

        // Use deleteById for deletion
        matchOddsRepository.deleteById(matchOddsEntity.getId());
        return true;
    }

    @Transactional
    public MatchOddsModel updateMatchOdd(MatchOddsModel matchOddsModel, UUID matchIdAsUuid) {
        MatchOddsEntity existingMatchOddsEntity = matchOddsRepository.findById(matchOddsModel.getId())
                                                                     .filter(odds -> odds.getMatch().getId().equals(matchIdAsUuid))
                                                                     .orElseThrow(() -> new ResourceNotFoundException("MatchOdd not found or does not belong to the specified match"));

        if (matchOddsModel.getOdd().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("Odds must be greater than zero.");
        }

        // Update fields
        existingMatchOddsEntity.setSpecifier(matchOddsModel.getSpecifier());
        existingMatchOddsEntity.setOdd(matchOddsModel.getOdd());

        // Save updated entity
        MatchOddsEntity updatedEntity = matchOddsRepository.save(existingMatchOddsEntity);
        return modelMapper.map(updatedEntity, MatchOddsModel.class);
    }
}
