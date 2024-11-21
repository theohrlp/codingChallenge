package com.example.challenge.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Match {
    private UUID id;
    @NotNull
    private String description;

    @NotNull(message = "Match date must not be null")
    @JsonProperty("match_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate matchDate;

    @NotNull
    @JsonProperty("match_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime matchTime;

    @NotNull
    @JsonProperty("team_a")
    private String teamA;

    @NotNull
    @JsonProperty("team_b")
    private String teamB;

    @NotNull
    private Sport sport;
}
