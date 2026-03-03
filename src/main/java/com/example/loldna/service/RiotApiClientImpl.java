package com.example.loldna.service;

import com.example.loldna.DTO.riot.RiotAccountDTO;
import com.example.loldna.DTO.riot.RiotLeagueEntryDTO;
import com.example.loldna.DTO.riot.RiotMatchDTO;
import com.example.loldna.DTO.riot.RiotSummonerDTO;
import com.example.loldna.config.RiotApiProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Service
public class RiotApiClientImpl implements RiotApiClient {

    private static final String ROUTING_REGION_BASE = "https://europe.api.riotgames.com";
    private static final String PLATFORM_REGION_BASE = "https://euw1.api.riotgames.com";

    private final RiotApiProperties riotApiProperties;
    private final RestTemplate restTemplate;

    public RiotApiClientImpl(RiotApiProperties riotApiProperties) {
        this.riotApiProperties = riotApiProperties;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public RiotAccountDTO getAccountByRiotId(String gameName, String tagLine) {
        String encodedGameName = URLEncoder.encode(gameName, StandardCharsets.UTF_8);
        String encodedTagLine = URLEncoder.encode(tagLine, StandardCharsets.UTF_8);

        String url = ROUTING_REGION_BASE
                + "/riot/account/v1/accounts/by-riot-id/"
                + encodedGameName + "/" + encodedTagLine;

        return get(url, RiotAccountDTO.class);
    }

    @Override
    public RiotSummonerDTO getSummonerByPuuid(String puuid) {
        String encodedPuuid = URLEncoder.encode(puuid, StandardCharsets.UTF_8);

        String url = PLATFORM_REGION_BASE
                + "/lol/summoner/v4/summoners/by-puuid/"
                + encodedPuuid;

        return get(url, RiotSummonerDTO.class);
    }

    @Override
    public List<String> getMatchIdsByPuuid(String puuid, int count) {
        String encodedPuuid = URLEncoder.encode(puuid, StandardCharsets.UTF_8);

        String url = ROUTING_REGION_BASE
                + "/lol/match/v5/matches/by-puuid/"
                + encodedPuuid + "/ids";

        String urlWithParams = UriComponentsBuilder
                .fromUriString(url)
                .queryParam("start", 0)
                .queryParam("count", count)
                .toUriString();

        String[] response = get(urlWithParams, String[].class);
        if (response == null) {
            return List.of();
        }
        return Arrays.asList(response);
    }

    @Override
    public RiotMatchDTO getMatchById(String matchId) {
        String encodedMatchId = URLEncoder.encode(matchId, StandardCharsets.UTF_8);

        String url = ROUTING_REGION_BASE
                + "/lol/match/v5/matches/"
                + encodedMatchId;

        return get(url, RiotMatchDTO.class);
    }

    @Override
    public List<RiotLeagueEntryDTO> getLeagueEntriesBySummonerId(String summonerId) {
        if (summonerId == null || summonerId.isBlank()) {
            return List.of();
        }

        String encodedSummonerId = URLEncoder.encode(summonerId, StandardCharsets.UTF_8);

        String url = PLATFORM_REGION_BASE
                + "/lol/league/v4/entries/by-summoner/"
                + encodedSummonerId;

        RiotLeagueEntryDTO[] entries = get(url, RiotLeagueEntryDTO[].class);
        if (entries == null) {
            return List.of();
        }
        return Arrays.asList(entries);
    }

    @Override
    public List<RiotLeagueEntryDTO> getLeagueEntriesByPuuid(String puuid) {
        if (puuid == null || puuid.isBlank()) {
            return List.of();
        }

        String encodedPuuid = URLEncoder.encode(puuid, StandardCharsets.UTF_8);

        String url = PLATFORM_REGION_BASE
                + "/lol/league/v4/entries/by-puuid/"
                + encodedPuuid;

        RiotLeagueEntryDTO[] entries = get(url, RiotLeagueEntryDTO[].class);
        if (entries == null) {
            return List.of();
        }
        return Arrays.asList(entries);
    }

    private <T> T get(String url, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", riotApiProperties.getKey());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<T> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                responseType
        );

        return response.getBody();
    }
}