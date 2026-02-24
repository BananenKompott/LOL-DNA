package com.example.loldna.dto;


import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
 public class MatchDTO implements Serializable 
{

    private static final long serialVersionUID = 1L;

    private String matchId;

    private LocalDate gameStartTime;

    private Integer gameDuration;

    private String queueType;

    private Double averageRank;

    private LocalDate cachedAt;


}
