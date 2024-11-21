package com.example.challenge.controllers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchOdds {
    private UUID id;
    @NotNull
    @JsonProperty("match_id")
    private UUID matchId;
    @NotNull
    private String specifier;
    @NotNull
    @Positive
    private BigDecimal odd;
}
