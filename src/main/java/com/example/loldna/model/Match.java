package com.example.loldna.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Match {

    @Id
    private String matchId;

    @Lob
    private String response;
}
