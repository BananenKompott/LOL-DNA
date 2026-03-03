package com.example.loldna.service;

import com.example.loldna.DTO.riot.RiotAccountDTO;
import com.example.loldna.DTO.riot.RiotLeagueEntryDTO;
import com.example.loldna.DTO.riot.RiotMatchDTO;
import com.example.loldna.DTO.riot.RiotSummonerDTO;

import java.util.List;

public interface RiotApiClient {

    RiotAccountDTO getAccountByRiotId(String gameName, String tagLine);

    RiotSummonerDTO getSummonerByPuuid(String puuid);

    List<String> getMatchIdsByPuuid(String puuid, int count);

    RiotMatchDTO getMatchById(String matchId);

    List<RiotLeagueEntryDTO> getLeagueEntriesBySummonerId(String summonerId);

    List<RiotLeagueEntryDTO> getLeagueEntriesByPuuid(String puuid);
}