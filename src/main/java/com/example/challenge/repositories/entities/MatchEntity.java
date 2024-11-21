package com.example.challenge.repositories.entities;

import com.example.challenge.controllers.dto.Sport;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "match")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MatchEntity {
    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "description", columnDefinition = "varchar")
    private String description;

    @Column(name = "team_a", columnDefinition = "varchar")
    @JsonProperty("team_a")
    private String teamA;

    @Column(name = "team_b", columnDefinition = "varchar")
    private String teamB;

    @Column(name = "match_date", columnDefinition = "date")
    private LocalDate matchDate;

    @Column(name = "match_time", columnDefinition = "time")
    private LocalTime matchTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "sport", columnDefinition = "varchar")
    private Sport sport;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchOddsEntity> odds = new ArrayList<>();
}
