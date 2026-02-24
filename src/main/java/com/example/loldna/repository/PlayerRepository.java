package com.example.loldna.repository;

import com.example.loldna.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByGameNameAndTagLine(String gameName, String tagLine);

    Optional<Player> findByPuuid(String puuid);
}
