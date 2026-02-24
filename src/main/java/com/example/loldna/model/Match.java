package com.example.loldna.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

import java.time.LocalDateTime;

@Entity
class Match {

    @Id
    private String matchId; // Riot match id

    private LocalDateTime gameStartTime;
    private Integer gameDuration;
    private String queueType;

    private Double averageRank; // Optional OZ01

    private LocalDateTime cachedAt;
}
