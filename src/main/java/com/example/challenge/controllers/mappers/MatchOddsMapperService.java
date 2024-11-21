package com.example.challenge.controllers.mappers;

import com.example.challenge.controllers.dto.MatchOdds;
import com.example.challenge.services.models.MatchOddsModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class MatchOddsMapperService {

    private final ModelMapper modelMapper;

    public MatchOddsMapperService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public MatchOddsModel mapToMatchModel(MatchOdds matchOdds) {
        return modelMapper.map(matchOdds, MatchOddsModel.class);
    }

    public MatchOdds mapToMatchOdd(MatchOddsModel matchOddsModel) {
        return modelMapper.map(matchOddsModel, MatchOdds.class);
    }
}
