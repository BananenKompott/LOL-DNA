package com.example.loldna.service;

import com.example.loldna.DTO.PlayerOverviewDTO;

public interface PlayerService {

    PlayerOverviewDTO getPlayerOverview(String gameName, String tagLine);
}
