package com.example.loldna.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class
MatchOverviewDTO {

    private String matchId;
    private LocalDateTime gameStartTime;

    private boolean win;

    private String championName;

    private int kills;
    private int deaths;
    private int assists;

    private int damageDealt;
    private int damageTaken;

    private List<TeamMateDTO> teamMates;

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public LocalDateTime getGameStartTime() {
        return gameStartTime;
    }

    public void setGameStartTime(LocalDateTime gameStartTime) {
        this.gameStartTime = gameStartTime;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public String getChampionName() {
        return championName;
    }

    public void setChampionName(String championName) {
        this.championName = championName;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getDamageDealt() {
        return damageDealt;
    }

    public void setDamageDealt(int damageDealt) {
        this.damageDealt = damageDealt;
    }

    public int getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(int damageTaken) {
        this.damageTaken = damageTaken;
    }

    public List<TeamMateDTO> getTeamMates() {
        return teamMates;
    }

    public void setTeamMates(List<TeamMateDTO> teamMates) {
        this.teamMates = teamMates;
    }
}
