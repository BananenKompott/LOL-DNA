package com.example.loldna.controller;

import com.example.loldna.DTO.PlayerOverviewDTO;
import com.example.loldna.service.PlayerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/{gameName}/{tagLine}")
    public PlayerOverviewDTO getPlayer(
            @PathVariable String gameName,
            @PathVariable String tagLine
    ) {
        return playerService.getPlayerOverview(gameName, tagLine);
    }
}
