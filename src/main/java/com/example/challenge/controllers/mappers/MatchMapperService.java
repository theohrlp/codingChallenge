package com.example.challenge.controllers.mappers;

import com.example.challenge.controllers.dto.Match;
import com.example.challenge.services.models.MatchModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class MatchMapperService {
    private final ModelMapper modelMapper;

    public MatchMapperService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public MatchModel mapToMatchModel(Match match) {
        return modelMapper.map(match, MatchModel.class);
    }

    public Match mapToMatch(MatchModel matchModel) {
        return modelMapper.map(matchModel, Match.class);
    }
}
