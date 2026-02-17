package com.example.loldna.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class PlayerMatchKey implements Serializable {

    @Column(name = "puuid")
    String puuid;

    @Column(name = "match_ID")
    String matchId;
}
