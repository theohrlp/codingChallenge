package com.example.challenge.controllers;

import com.example.challenge.services.exceptions.InvalidMatchIdGiven;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

class GenericExceptionHandlerTest {

    private final GenericExceptionHandler handler = new GenericExceptionHandler();

    @Test
    void invalidMatchIdGiven_returnsUnprocessableEntity() {
        // arrange
        UUID id = UUID.randomUUID();
        var ex = new InvalidMatchIdGiven("Match id inside odds body does not match the match id given");

        // act
        ResponseEntity<String> result = handler.handleInvalidMatchIdGiven(ex);

        // assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, result.getStatusCode());
        assertEquals("Match id inside odds body does not match the match id given", result.getBody());
    }
}
