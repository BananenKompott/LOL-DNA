package com.example.loldna.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Player {
    @Id
    @Column(name = "puuid")
    private String puuid;

    @Column(name = "gameName")
    private String gameName;

    @Column(name = "tagLine")
    private String tagLine;
}
