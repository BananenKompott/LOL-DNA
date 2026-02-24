package com.example.loldna.repository;

import com.example.loldna.entity.MatchParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long> {

    @Query("""
        SELECT mp
        FROM MatchParticipant mp
        JOIN FETCH mp.match m
        WHERE mp.puuid = :puuid
        ORDER BY m.gameStartTime DESC
    """)
    List<MatchParticipant> findLatestMatchesByPuuid(String puuid);
}
