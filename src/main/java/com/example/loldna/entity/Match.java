package com.example.loldna.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Match {

    @Id
    private String matchId; // Riot match id

    private LocalDateTime gameStartTime;
    private Integer gameDuration;
    private String queueType;

    private String averageRank; // Optional OZ01

    private LocalDateTime cachedAt;
}
