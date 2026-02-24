package com.example.loldna.DTO;

import java.util.List;

public class PlayerOverviewDTO {

    private String gameName;
    private String tagLine;

    private String tier;
    private String rank;
    private Integer leaguePoints;

    private Double winRateLast20;

    private List<MatchOverviewDTO> matches;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Integer getLeaguePoints() {
        return leaguePoints;
    }

    public void setLeaguePoints(Integer leaguePoints) {
        this.leaguePoints = leaguePoints;
    }

    public Double getWinRateLast20() {
        return winRateLast20;
    }

    public void setWinRateLast20(Double winRateLast20) {
        this.winRateLast20 = winRateLast20;
    }

    public List<MatchOverviewDTO> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchOverviewDTO> matches) {
        this.matches = matches;
    }
}
