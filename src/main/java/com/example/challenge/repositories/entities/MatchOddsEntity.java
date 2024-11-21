package com.example.challenge.repositories.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "match_odds")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MatchOddsEntity {
    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "specifier", columnDefinition = "varchar")
    private String specifier;

    @Column(name = "odd", columnDefinition = "numeric(5, 2)")
    private BigDecimal odd;

    @ManyToOne
    @JoinColumn(name = "match_id", referencedColumnName = "id", nullable = false)
    private MatchEntity match;
}
