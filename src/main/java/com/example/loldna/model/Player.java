package com.example.loldna.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
class Player {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String puuid; // Riot's universal player id

    private String gameName;   // Nickname
    private String tagLine;

    private String summonerId;
    private String profileIconId;

    private String tier;       // GOLD, PLATINUM...
    private String rank;       // I, II, III...
    private Integer leaguePoints;

    private LocalDateTime lastUpdated;
}
