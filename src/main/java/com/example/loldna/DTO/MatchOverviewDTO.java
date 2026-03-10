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

    // OZ01 / MZ03: additional per-match & per-player metrics
    private Double goldPerMinute;
    private Double csPerMinute;
    private Integer laneQuestCompletionTimeInSeconds;

    // Optional: average rank of all team mates in this match (if available on Match entity)
    private String averageRank;

    // MZ03f / OZ01e: runes and items of the searched player in this match
    // runeIds kept for backward compatibility, but preferred consumer field is runes
    private List<Integer> runeIds;
    private List<RuneDTO> runes;
    private List<Integer> itemIds;

    private List<TeamMateDTO> teamMates;
    private List<EnemyDTO> enemyTeam;

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
// BIG Bubensahne ist geil
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

    public String getAverageRank() {
        return averageRank;
    }

    public void setAverageRank(String averageRank) {
        this.averageRank = averageRank;
    }

    public List<Integer> getRuneIds() {
        return runeIds;
    }

    public void setRuneIds(List<Integer> runeIds) {
        this.runeIds = runeIds;
    }

    public List<Integer> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Integer> itemIds) {
        this.itemIds = itemIds;
    }

    public List<RuneDTO> getRunes() {
        return runes;
    }

    public void setRunes(List<RuneDTO> runes) {
        this.runes = runes;
    }

    public List<TeamMateDTO> getTeamMates() {
        return teamMates;
    }

    public void setTeamMates(List<TeamMateDTO> teamMates) {
        this.teamMates = teamMates;
    }

    public List<EnemyDTO> getEnemyTeam() {
        return enemyTeam;
    }

    public void setEnemyTeam(List<EnemyDTO> enemyTeam) {
        this.enemyTeam = enemyTeam;
    }
}