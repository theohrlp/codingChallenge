package com.example.challenge.services.models;

import com.example.challenge.controllers.dto.Sport;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MatchModel {
    private UUID id;

    @NotNull
    private String description;

    @NotNull
    @JsonProperty("team_a")
    private String teamA;

    @NotNull
    @JsonProperty("team_b")
    private String teamB;

    @NotNull
    @JsonProperty("match_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate matchDate;

    @NotNull
    @JsonProperty("match_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime matchTime;

    @NotNull
    private Sport sport;
}
