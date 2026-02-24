package com.example.loldna.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
class ParticipantItem {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private MatchParticipant participant;

    private Integer itemId;
}
