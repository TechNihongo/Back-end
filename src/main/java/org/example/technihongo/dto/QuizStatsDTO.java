package org.example.technihongo.dto;

import lombok.Data;

@Data
public class QuizStatsDTO {
    private String date;
    private Double averageScore;

    public QuizStatsDTO(String date, Double averageScore) {
        this.date = date;
        this.averageScore = averageScore != null ? averageScore : 0.0;
    }
}
