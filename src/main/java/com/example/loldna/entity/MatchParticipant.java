package com.example.loldna.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class MatchParticipant {

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

    private Integer laneQuestCompletionTimeInSeconds; // optional

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
    private List<ParticipantItem> items;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
    private List<ParticipantRune> runes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public String getPuuid() {
        return puuid;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    public String getChampionName() {
        return championName;
    }

    public void setChampionName(String championName) {
        this.championName = championName;
    }

    public Integer getKills() {
        return kills;
    }

    public void setKills(Integer kills) {
        this.kills = kills;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getAssists() {
        return assists;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }

    public Boolean getWin() {
        return win;
    }

    public void setWin(Boolean win) {
        this.win = win;
    }

    public Integer getTotalDamageDealt() {
        return totalDamageDealt;
    }

    public void setTotalDamageDealt(Integer totalDamageDealt) {
        this.totalDamageDealt = totalDamageDealt;
    }

    public Integer getTotalDamageTaken() {
        return totalDamageTaken;
    }

    public void setTotalDamageTaken(Integer totalDamageTaken) {
        this.totalDamageTaken = totalDamageTaken;
    }

    public Integer getGoldEarned() {
        return goldEarned;
    }

    public void setGoldEarned(Integer goldEarned) {
        this.goldEarned = goldEarned;
    }

    public Integer getTotalMinionsKilled() {
        return totalMinionsKilled;
    }

    public void setTotalMinionsKilled(Integer totalMinionsKilled) {
        this.totalMinionsKilled = totalMinionsKilled;
    }

    public Double getGoldPerMinute() {
        return goldPerMinute;
    }

    public void setGoldPerMinute(Double goldPerMinute) {
        this.goldPerMinute = goldPerMinute;
    }

    public Double getCsPerMinute() {
        return csPerMinute;
    }

    public void setCsPerMinute(Double csPerMinute) {
        this.csPerMinute = csPerMinute;
    }

    public Integer getLaneQuestCompletionTimeInSeconds() {
        return laneQuestCompletionTimeInSeconds;
    }

    public void setLaneQuestCompletionTimeInSeconds(Integer laneQuestCompletionTimeInSeconds) {
        this.laneQuestCompletionTimeInSeconds = laneQuestCompletionTimeInSeconds;
    }

    public List<ParticipantItem> getItems() {
        return items;
    }

    public void setItems(List<ParticipantItem> items) {
        this.items = items;
    }

    public List<ParticipantRune> getRunes() {
        return runes;
    }

    public void setRunes(List<ParticipantRune> runes) {
        this.runes = runes;
    }
}