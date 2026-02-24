package com.example.loldna.service;

import com.example.loldna.DTO.*;
import com.example.loldna.entity.*;
import com.example.loldna.exception.PlayerNotFoundException;
import com.example.loldna.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final MatchParticipantRepository participantRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository, MatchParticipantRepository participantRepository) {
        this.playerRepository = playerRepository;
        this.participantRepository = participantRepository;
    }

    @Override
    public PlayerOverviewDTO getPlayerOverview(String gameName, String tagLine) {

        Player player = null;
        try {
            player = playerRepository
                    .findByGameNameAndTagLine(gameName, tagLine)
                    .orElseThrow(() -> new PlayerNotFoundException("Player not found"));
        } catch (PlayerNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<MatchParticipant> participants =
                participantRepository.findLatestMatchesByPuuid(player.getPuuid())
                        .stream()
                        .limit(20)
                        .toList();

        return mapToDTO(player, participants);
    }

    private PlayerOverviewDTO mapToDTO(Player player, List<MatchParticipant> participants) {

        PlayerOverviewDTO dto = new PlayerOverviewDTO();
        dto.setGameName(player.getGameName());
        dto.setTagLine(player.getTagLine());
        dto.setTier(player.getTier());
        dto.setRank(player.getRank());
        dto.setLeaguePoints(player.getLeaguePoints());

        List<MatchOverviewDTO> matches = participants.stream()
                .map(this::mapMatch)
                .collect(Collectors.toList());

        dto.setMatches(matches);

        long wins = participants.stream().filter(MatchParticipant::getWin).count();
        dto.setWinRateLast20(
                participants.isEmpty() ? 0 :
                        (wins * 100.0) / participants.size()
        );

        return dto;
    }

    private MatchOverviewDTO mapMatch(MatchParticipant participant) {

        MatchOverviewDTO dto = new MatchOverviewDTO();

        dto.setMatchId(participant.getMatch().getMatchId());
        dto.setGameStartTime(participant.getMatch().getGameStartTime());
        dto.setWin(participant.getWin());

        dto.setChampionName(participant.getChampionName());
        dto.setKills(participant.getKills());
        dto.setDeaths(participant.getDeaths());
        dto.setAssists(participant.getAssists());

        dto.setDamageDealt(participant.getTotalDamageDealt());
        dto.setDamageTaken(participant.getTotalDamageTaken());

        return dto;
    }
}
