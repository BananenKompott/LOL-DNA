package com.example.loldna.DTO.riot;

import java.util.List;

public class RiotMatchDTO {

    private Metadata metadata;
    private Info info;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public static class Metadata {
        private String matchId;

        public String getMatchId() {
            return matchId;
        }

        public void setMatchId(String matchId) {
            this.matchId = matchId;
        }
    }

    public static class Info {
        private Long gameStartTimestamp;
        private Integer gameDuration;
        private List<Participant> participants;

        public Long getGameStartTimestamp() {
            return gameStartTimestamp;
        }

        public void setGameStartTimestamp(Long gameStartTimestamp) {
            this.gameStartTimestamp = gameStartTimestamp;
        }

        public Integer getGameDuration() {
            return gameDuration;
        }

        public void setGameDuration(Integer gameDuration) {
            this.gameDuration = gameDuration;
        }

        public List<Participant> getParticipants() {
            return participants;
        }

        public void setParticipants(List<Participant> participants) {
            this.participants = participants;
        }
    }

    public static class Participant {
        private String puuid;

        // NEW: encryptedSummonerId from match-v5 payload (needed for league-v4)
        private String summonerId;

        private String championName;

        private String riotIdGameName;
        private String riotIdTagline;

        private int teamId;

        private int kills;
        private int deaths;
        private int assists;

        private boolean win;

        private int totalDamageDealtToChampions;
        private int totalDamageTaken;

        private int goldEarned;
        private int totalMinionsKilled;

        // Runes (perks) as provided by Match V5 (MZ03f / OZ01e)
        // Real payload (simplified):
        // "perks": {
        //   "styles": [
        //     { "selections": [ { "perk": 8005, ... }, ... ], "style": 8000 },
        //     { "selections": [ { "perk": 8100, ... }, ... ], "style": 8100 }
        //   ]
        // }
        private Perks perks;

        public static class Perks {
            private List<Style> styles;

            public List<Style> getStyles() {
                return styles;
            }

            public void setStyles(List<Style> styles) {
                this.styles = styles;
            }
        }

        public static class Style {
            private List<Selection> selections;
            private Integer style;

            public List<Selection> getSelections() {
                return selections;
            }

            public void setSelections(List<Selection> selections) {
                this.selections = selections;
            }

            public Integer getStyle() {
                return style;
            }

            public void setStyle(Integer style) {
                this.style = style;
            }
        }

        public static class Selection {
            private Integer perk;

            public Integer getPerk() {
                return perk;
            }

            public void setPerk(Integer perk) {
                this.perk = perk;
            }
        }

        public String getPuuid() {
            return puuid;
        }

        public void setPuuid(String puuid) {
            this.puuid = puuid;
        }

        public String getSummonerId() {
            return summonerId;
        }

        public void setSummonerId(String summonerId) {
            this.summonerId = summonerId;
        }

        public String getChampionName() {
            return championName;
        }

        public void setChampionName(String championName) {
            this.championName = championName;
        }

        public String getRiotIdGameName() {
            return riotIdGameName;
        }

        public void setRiotIdGameName(String riotIdGameName) {
            this.riotIdGameName = riotIdGameName;
        }

        public String getRiotIdTagline() {
            return riotIdTagline;
        }

        public void setRiotIdTagline(String riotIdTagline) {
            this.riotIdTagline = riotIdTagline;
        }

        public int getTeamId() {
            return teamId;
        }

        public void setTeamId(int teamId) {
            this.teamId = teamId;
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

        public boolean isWin() {
            return win;
        }

        public void setWin(boolean win) {
            this.win = win;
        }

        public int getTotalDamageDealtToChampions() {
            return totalDamageDealtToChampions;
        }

        public void setTotalDamageDealtToChampions(int totalDamageDealtToChampions) {
            this.totalDamageDealtToChampions = totalDamageDealtToChampions;
        }

        public int getTotalDamageTaken() {
            return totalDamageTaken;
        }

        public void setTotalDamageTaken(int totalDamageTaken) {
            this.totalDamageTaken = totalDamageTaken;
        }

        public int getGoldEarned() {
            return goldEarned;
        }

        public void setGoldEarned(int goldEarned) {
            this.goldEarned = goldEarned;
        }

        public int getTotalMinionsKilled() {
            return totalMinionsKilled;
        }

        public void setTotalMinionsKilled(int totalMinionsKilled) {
            this.totalMinionsKilled = totalMinionsKilled;
        }

        public Perks getPerks() {
            return perks;
        }

        public void setPerks(Perks perks) {
            this.perks = perks;
        }
    }
}