package com.example.loldna.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
class MatchParticipant {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Match match;

    private String puuid;

    private String championName;

    private Integer kills;
    private Integer deaths;
    private Integer assists;

    private Boolean win;

    private Integer totalDamageDealt;
    private Integer totalDamageTaken;

    private Integer goldEarned;
    private Integer totalMinionsKilled;

    private Double goldPerMinute;
    private Double csPerMinute;

    private Integer laneQuestCompletionTime; // optional

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
    private List<ParticipantItem> items;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
    private List<ParticipantRune> runes;
}