package com.example.challenge.controllers;

import com.example.challenge.controllers.dto.Match;
import com.example.challenge.controllers.mappers.MatchMapperService;
import com.example.challenge.services.MatchService;
import com.example.challenge.services.models.MatchModel;
import com.example.challenge.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class MatchesControllerTest {

    @InjectMocks
    private MatchesController matchesController;

    @Mock
    private MatchService matchService;

    @Mock
    private MatchMapperService matchMapperService;


    @Test
    void getMatch_shouldReturnMatch_whenMatchExists() {
        // arrange
        var matchId = UUID.randomUUID();
        var matchModel = new MatchModel(matchId, "Description", "Team A", "Team B", null, null, null);
        var match = new Match(matchId, "Description", null, null, "Team A", "Team B", null);
        var expected = new Match(matchId, "Description", null, null, "Team A", "Team B", null);

        when(matchService.getMatch(matchId)).thenReturn(Optional.of(matchModel));
        when(matchMapperService.mapToMatch(any(MatchModel.class))).thenReturn(match);


        // act
        ResponseEntity<Match> result = matchesController.getMatch(matchId.toString());

        // assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expected, result.getBody());
    }

    @Test
    void getMatch_shouldReturnNotFound_whenMatchDoesNotExist() {
        // arrange
        var matchId = UUID.randomUUID();
        when(matchService.getMatch(matchId)).thenReturn(Optional.empty());

        // act & assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                matchesController.getMatch(matchId.toString()));
        assertEquals("Match not found with ID: " + matchId, exception.getMessage());
    }

}

