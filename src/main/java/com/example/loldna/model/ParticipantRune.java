package com.example.loldna.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
class ParticipantRune {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private MatchParticipant participant;

    private Integer runeId;
}