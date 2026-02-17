package com.example.loldna.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class PlayerMatches {
    @EmbeddedId
    PlayerMatchKey id;

    @ManyToOne
    @JoinColumn(name = "player_puuid")
    Player player;

    @ManyToOne
    @JoinColumn(name = "match_match_id")
    Match match;

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
