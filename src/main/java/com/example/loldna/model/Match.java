package com.example.loldna.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Match {

    @Id
    @Column(name = "match_ID")
    private String matchId;

    @Lob
    @Column(name = "response")
    private String response;
}
