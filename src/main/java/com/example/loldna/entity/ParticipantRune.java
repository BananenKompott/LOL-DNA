package com.example.loldna.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ParticipantRune {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private MatchParticipant participant;

    private Integer runeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MatchParticipant getParticipant() {
        return participant;
    }

    public void setParticipant(MatchParticipant participant) {
        this.participant = participant;
    }

    public Integer getRuneId() {
        return runeId;
    }

    public void setRuneId(Integer runeId) {
        this.runeId = runeId;
    }
}