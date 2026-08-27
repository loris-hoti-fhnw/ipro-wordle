package fhnw.hoti.worldebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatisticsResponse {

    private long gamesPlayed;
    private long totalAttempts;
    private double averageAttempts;
}