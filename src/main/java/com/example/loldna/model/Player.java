package com.example.loldna.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Player {
    @Id
    private String puuid;

    private String gameName;

    private String tagLine;
}
