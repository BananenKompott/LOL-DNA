package com.example.loldna.service;

import com.example.loldna.DTO.EnemyDTO;
import com.example.loldna.DTO.MatchOverviewDTO;
import com.example.loldna.DTO.PlayerOverviewDTO;
import com.example.loldna.DTO.TeamMateDTO;
import com.example.loldna.DTO.RuneDTO;
import com.example.loldna.DTO.riot.RiotAccountDTO;
import com.example.loldna.DTO.riot.RiotLeagueEntryDTO;
import com.example.loldna.DTO.riot.RiotMatchDTO;
import com.example.loldna.entity.Match;
import com.example.loldna.entity.MatchParticipant;
import com.example.loldna.entity.ParticipantItem;
import com.example.loldna.entity.ParticipantRune;
import com.example.loldna.entity.Player;
import com.example.loldna.exception.PlayerNotFoundException;
import com.example.loldna.repository.MatchParticipantRepository;
import com.example.loldna.repository.MatchRepository;
import com.example.loldna.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final Logger log = LoggerFactory.getLogger(PlayerServiceImpl.class);

    private static final Duration RANK_TTL = Duration.ofHours(6);
    private static final int MAX_RANK_FETCHES_PER_REQUEST = 8;

    private final PlayerRepository playerRepository;
    private final MatchParticipantRepository participantRepository;
    private final MatchRepository matchRepository;
    private final RiotApiClient riotApiClient;
    private final RuneLookupService runeLookupService;

    public PlayerServiceImpl(
            PlayerRepository playerRepository,
            MatchParticipantRepository participantRepository,
            MatchRepository matchRepository,
            RiotApiClient riotApiClient,
            RuneLookupService runeLookupService
    ) {
        this.playerRepository = playerRepository;
        this.participantRepository = participantRepository;
        this.matchRepository = matchRepository;
        this.riotApiClient = riotApiClient;
        this.runeLookupService = runeLookupService;
    }

    @Override
    public PlayerOverviewDTO getPlayerOverview(String gameName, String tagLine) {

        Player player = playerRepository
                .findByGameNameAndTagLine(gameName, tagLine)
                .orElseGet(() -> fetchAndCachePlayer(gameName, tagLine));

        if (player == null) {
            throw new PlayerNotFoundException("Player not found");
        }

        RankFetchBudget budget = new RankFetchBudget(MAX_RANK_FETCHES_PER_REQUEST);
        Map<String, Player> localRankCache = new HashMap<>();

        player = ensureRankCached(player.getPuuid(), budget, localRankCache);

        List<MatchParticipant> participants =
                participantRepository.findLatestMatchesByPuuid(player.getPuuid())
                        .stream()
                        .limit(20)
                        .toList();

        return mapToDTO(player, participants, budget, localRankCache);
    }

    private Player fetchAndCachePlayer(String gameName, String tagLine) {

        RiotAccountDTO account = riotApiClient.getAccountByRiotId(gameName, tagLine);
        if (account == null || account.getPuuid() == null) {
            return null;
        }

        String puuid = account.getPuuid();

        Player player = playerRepository.findByPuuid(puuid).orElseGet(Player::new);
        player.setPuuid(puuid);
        player.setGameName(account.getGameName());
        player.setTagLine(account.getTagLine());
        player.setLastUpdated(LocalDateTime.now());

        Player savedPlayer = playerRepository.save(player);

        List<String> matchIds = riotApiClient.getMatchIdsByPuuid(puuid, 20);
        for (String matchId : matchIds) {
            if (!matchRepository.existsById(matchId)) {
                RiotMatchDTO matchDTO = riotApiClient.getMatchById(matchId);
                if (matchDTO != null && matchDTO.getInfo() != null) {
                    persistMatchFromDto(matchDTO);
                }
            }
        }

        return savedPlayer;
    }

    /**
     * Rank semantics (your requirement):
     *  - rank = BRONZE/SILVER/GOLD/...  (Riot field: "tier")
     *  - tier = I/II/III/IV            (Riot field: "rank")
     */
    private Player ensureRankCached(String puuid, RankFetchBudget budget, Map<String, Player> localRankCache) {
        if (puuid == null || puuid.isBlank()) {
            Player p = new Player();
            p.setPuuid(puuid);
            p.setRank("UNRANKED");
            p.setTier(null);
            return p;
        }

        Player cachedInRequest = localRankCache.get(puuid);
        if (cachedInRequest != null) {
            return cachedInRequest;
        }

        Player player = playerRepository.findByPuuid(puuid).orElseGet(() -> {
            Player p = new Player();
            p.setPuuid(puuid);
            p.setLastUpdated(LocalDateTime.now());
            return playerRepository.save(p);
        });

        if (isRankFresh(player)) {
            log.debug("Rank cache HIT for puuid={}, rank={}, tier={}, lastUpdated={}",
                    puuid, player.getRank(), player.getTier(), player.getLastUpdated());
            localRankCache.put(puuid, player);
            return player;
        }

        if (!budget.trySpend()) {
            log.debug("Rank fetch budget exhausted; skipping Riot fetch for puuid={}", puuid);
            if (player.getRank() == null || player.getRank().isBlank()) {
                player.setRank("UNRANKED");
                player.setTier(null);
            }
            localRankCache.put(puuid, player);
            return player;
        }

        try {
            log.debug("Fetching rank from Riot for puuid={} (League-V4 by-puuid)", puuid);

            List<RiotLeagueEntryDTO> leagueEntries = riotApiClient.getLeagueEntriesByPuuid(puuid);

            Optional<RiotLeagueEntryDTO> solo = leagueEntries.stream()
                    .filter(e -> "RANKED_SOLO_5x5".equalsIgnoreCase(e.getQueueType()))
                    .findFirst();

            if (solo.isPresent()) {
                RiotLeagueEntryDTO e = solo.get();
                player.setRank(e.getTier()); // GOLD/SILVER/...
                player.setTier(e.getRank()); // I/II/III/IV
                player.setLeaguePoints(e.getLeaguePoints());
            } else {
                player.setRank("UNRANKED");
                player.setTier(null);
                player.setLeaguePoints(null);
            }

            player.setLastUpdated(LocalDateTime.now());
            player = playerRepository.save(player);
        } catch (Exception ex) {
            log.warn("Rank fetch failed for puuid={} (will use cached/UNRANKED). Error={}", puuid, ex.toString());
            if (player.getRank() == null || player.getRank().isBlank()) {
                player.setRank("UNRANKED");
                player.setTier(null);
            }
            player = playerRepository.save(player);
        }

        localRankCache.put(puuid, player);
        return player;
    }

    private boolean isRankFresh(Player player) {
        if (player == null) return false;
        if (player.getRank() == null || player.getRank().isBlank()) return false;
        if (player.getLastUpdated() == null) return false;
        return Duration.between(player.getLastUpdated(), LocalDateTime.now()).compareTo(RANK_TTL) < 0;
    }

    private void persistMatchFromDto(RiotMatchDTO matchDTO) {
        RiotMatchDTO.Info info = matchDTO.getInfo();

        Match match = new Match();
        match.setMatchId(matchDTO.getMetadata().getMatchId());

        if (info.getGameStartTimestamp() != null) {
            LocalDateTime startTime = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(info.getGameStartTimestamp()),
                    ZoneId.systemDefault()
            );
            match.setGameStartTime(startTime);
        }

        match.setGameDuration(info.getGameDuration());
        match.setCachedAt(LocalDateTime.now());

        Match savedMatch = matchRepository.save(match);

        if (info.getParticipants() != null) {
            List<MatchParticipant> participants = info.getParticipants().stream()
                    .map(p -> {
                        MatchParticipant mp = new MatchParticipant();
                        mp.setMatch(savedMatch);
                        mp.setPuuid(p.getPuuid());
                        mp.setGameName(p.getRiotIdGameName());
                        mp.setTagLine(p.getRiotIdTagline());
                        mp.setTeamId(p.getTeamId());
                        mp.setChampionName(p.getChampionName());
                        mp.setKills(p.getKills());
                        mp.setDeaths(p.getDeaths());
                        mp.setAssists(p.getAssists());
                        mp.setWin(p.isWin());
                        mp.setTotalDamageDealt(p.getTotalDamageDealtToChampions());
                        mp.setTotalDamageTaken(p.getTotalDamageTaken());
                        mp.setGoldEarned(p.getGoldEarned());
                        mp.setTotalMinionsKilled(p.getTotalMinionsKilled());

                        if (info.getGameDuration() != null && info.getGameDuration() > 0) {
                            double minutes = info.getGameDuration() / 60.0;
                            mp.setGoldPerMinute(p.getGoldEarned() / minutes);
                            mp.setCsPerMinute(p.getTotalMinionsKilled() / minutes);
                        }

                        // Map rune IDs from Match V5 perks into ParticipantRune entities
                        RiotMatchDTO.Participant.Perks perks = p.getPerks();
                        if (perks != null && perks.getStyles() != null) {
                            List<ParticipantRune> runes = perks.getStyles().stream()
                                    .filter(Objects::nonNull)
                                    .flatMap(style -> {
                                        if (style.getSelections() == null) {
                                            return java.util.stream.Stream.<ParticipantRune>empty();
                                        }
                                        return style.getSelections().stream()
                                                .filter(Objects::nonNull)
                                                .map(sel -> sel.getPerk())
                                                .filter(Objects::nonNull)
                                                .map(runeId -> {
                                                    ParticipantRune r = new ParticipantRune();
                                                    r.setParticipant(mp);
                                                    r.setRuneId(runeId);
                                                    return r;
                                                });
                                    })
                                    .collect(Collectors.toList());

                            if (!runes.isEmpty()) {
                                mp.setRunes(runes);
                            }
                        }

                        return mp;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            participantRepository.saveAll(participants);
        }
    }

    private PlayerOverviewDTO mapToDTO(
            Player player,
            List<MatchParticipant> participants,
            RankFetchBudget budget,
            Map<String, Player> localRankCache
    ) {

        PlayerOverviewDTO dto = new PlayerOverviewDTO();
        dto.setGameName(player.getGameName());
        dto.setTagLine(player.getTagLine());
        dto.setRank(player.getRank());
        dto.setTier(player.getTier());
        dto.setLeaguePoints(player.getLeaguePoints());

        List<MatchOverviewDTO> matches = participants.stream()
                .map(p -> mapMatch(p, budget, localRankCache))
                .collect(Collectors.toList());

        dto.setMatches(matches);

        long wins = participants.stream().filter(mp -> Boolean.TRUE.equals(mp.getWin())).count();
        dto.setWinRateLast20(participants.isEmpty() ? 0 : (wins * 100.0) / participants.size());

        return dto;
    }

    private MatchOverviewDTO mapMatch(
            MatchParticipant participant,
            RankFetchBudget budget,
            Map<String, Player> localRankCache
    ) {

        MatchOverviewDTO dto = new MatchOverviewDTO();

        Match match = participant.getMatch();

        dto.setMatchId(match != null ? match.getMatchId() : null);
        dto.setGameStartTime(match != null ? match.getGameStartTime() : null);
        dto.setWin(Boolean.TRUE.equals(participant.getWin()));

        dto.setChampionName(participant.getChampionName());
        dto.setKills(participant.getKills() == null ? 0 : participant.getKills());
        dto.setDeaths(participant.getDeaths() == null ? 0 : participant.getDeaths());
        dto.setAssists(participant.getAssists() == null ? 0 : participant.getAssists());

        dto.setDamageDealt(participant.getTotalDamageDealt() == null ? 0 : participant.getTotalDamageDealt());
        dto.setDamageTaken(participant.getTotalDamageTaken() == null ? 0 : participant.getTotalDamageTaken());

        // OZ01: per-match and per-player metrics
        dto.setGoldPerMinute(participant.getGoldPerMinute());
        dto.setCsPerMinute(participant.getCsPerMinute());
        dto.setLaneQuestCompletionTimeInSeconds(participant.getLaneQuestCompletionTimeInSeconds());

        if (match != null) {
            dto.setAverageRank(match.getAverageRank());
        }

        // MZ03f / OZ01e: runes & items of the searched player in this match
        if (participant.getRunes() != null) {
            List<Integer> runeIds = participant.getRunes().stream()
                    .filter(Objects::nonNull)
                    .map(ParticipantRune::getRuneId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            dto.setRuneIds(runeIds);

            // Resolve rune IDs to names/icons via Data Dragon
            List<RuneLookupService.RuneInfo> infos = runeLookupService.resolveRunes(runeIds);
            if (infos != null && !infos.isEmpty()) {
                List<RuneDTO> runes = infos.stream()
                        .map(info -> {
                            RuneDTO r = new RuneDTO();
                            r.setId(info.getId());
                            r.setName(info.getName());
                            r.setIcon(info.getIcon());
                            return r;
                        })
                        .collect(Collectors.toList());
                dto.setRunes(runes);
            }
        }

        if (participant.getItems() != null) {
            dto.setItemIds(
                    participant.getItems().stream()
                            .filter(Objects::nonNull)
                            .map(ParticipantItem::getItemId)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList())
            );
        }

        if (participant.getMatch() != null && participant.getTeamId() != null) {
            List<MatchParticipant> allParticipants =
                    participantRepository.findByMatch_MatchId(participant.getMatch().getMatchId());

            List<TeamMateDTO> teamMates = allParticipants.stream()
                    .filter(p -> p.getTeamId() != null)
                    .filter(p -> p.getTeamId().equals(participant.getTeamId()))
                    .filter(p -> p.getPuuid() != null && !p.getPuuid().equals(participant.getPuuid()))
                    .map(p -> {
                        TeamMateDTO t = new TeamMateDTO();
                        t.setGameName(p.getGameName());
                        t.setTagLine(p.getTagLine());
                        t.setChampionName(p.getChampionName());

                        int kills = p.getKills() == null ? 0 : p.getKills();
                        int deaths = p.getDeaths() == null ? 0 : p.getDeaths();
                        int assists = p.getAssists() == null ? 0 : p.getAssists();

                        t.setKills(kills);
                        t.setDeaths(deaths);
                        t.setAssists(assists);

                        t.setDamageDealt(p.getTotalDamageDealt() == null ? 0 : p.getTotalDamageDealt());
                        t.setDamageTaken(p.getTotalDamageTaken() == null ? 0 : p.getTotalDamageTaken());

                        double kda = (kills + assists) / (double) Math.max(1, deaths);
                        t.setKda(kda);

                        Player ranked = ensureRankCached(p.getPuuid(), budget, localRankCache);
                        t.setRank(ranked.getRank());
                        t.setTier(ranked.getTier());
                        t.setLeaguePoints(ranked.getLeaguePoints());

                        return t;
                    })
                    .collect(Collectors.toList());

            dto.setTeamMates(teamMates);

            List<EnemyDTO> enemyTeam = allParticipants.stream()
                    .filter(p -> p.getTeamId() != null)
                    .filter(p -> !p.getTeamId().equals(participant.getTeamId()))
                    .map(p -> {
                        EnemyDTO e = new EnemyDTO();
                        e.setGameName(p.getGameName());
                        e.setTagLine(p.getTagLine());
                        e.setChampionName(p.getChampionName());

                        int kills = p.getKills() == null ? 0 : p.getKills();
                        int deaths = p.getDeaths() == null ? 0 : p.getDeaths();
                        int assists = p.getAssists() == null ? 0 : p.getAssists();

                        e.setKills(kills);
                        e.setDeaths(deaths);
                        e.setAssists(assists);

                        e.setDamageDealt(p.getTotalDamageDealt() == null ? 0 : p.getTotalDamageDealt());
                        e.setDamageTaken(p.getTotalDamageTaken() == null ? 0 : p.getTotalDamageTaken());

                        double kda = (kills + assists) / (double) Math.max(1, deaths);
                        e.setKda(kda);

                        Player ranked = ensureRankCached(p.getPuuid(), budget, localRankCache);
                        e.setRank(ranked.getRank());
                        e.setTier(ranked.getTier());
                        e.setLeaguePoints(ranked.getLeaguePoints());

                        return e;
                    })
                    .collect(Collectors.toList());

            dto.setEnemyTeam(enemyTeam);
        }

        return dto;
    }

    private static final class RankFetchBudget {
        private int remaining;

        private RankFetchBudget(int remaining) {
            this.remaining = Math.max(0, remaining);
        }

        private boolean trySpend() {
            if (remaining <= 0) return false;
            remaining--;
            return true;
        }
    }
}