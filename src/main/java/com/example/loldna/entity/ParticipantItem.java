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

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}
