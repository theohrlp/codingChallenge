package com.example.challenge.services;

import com.example.challenge.repositories.MatchRepository;
import com.example.challenge.repositories.entities.MatchEntity;
import com.example.challenge.services.models.MatchModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @InjectMocks
    private MatchService matchService;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void getMatch_shouldReturnMatch_whenMatchExists() {
        // Arrange
        var id = UUID.randomUUID();
        var matchModel = new MatchModel(id, "Description", "Team A", "Team B", null, null, null); // Expected result
        var matchEntity = new MatchEntity();
        matchEntity.setId(id);

        when(matchRepository.findById(id)).thenReturn(Optional.of(matchEntity));
        when(modelMapper.map(matchEntity, MatchModel.class)).thenReturn(matchModel); // Mock mapping explicitly

        // Act
        Optional<MatchModel> result = matchService.getMatch(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(matchModel, result.get());
        verify(matchRepository).findById(id);
        verify(modelMapper).map(matchEntity, MatchModel.class);
    }


    @Test
    void getMatch_shouldReturnEmpty_whenMatchDoesNotExist() {
        // Arrange
        UUID matchId = UUID.randomUUID();
        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        // Act
        Optional<MatchModel> result = matchService.getMatch(matchId);

        // Assert
        assertFalse(result.isPresent());
        verify(matchRepository).findById(matchId);
    }
}

