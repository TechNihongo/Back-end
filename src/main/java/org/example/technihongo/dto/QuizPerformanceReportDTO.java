package org.example.technihongo.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizPerformanceReportDTO {
    private Integer quizId;
    private String quizTitle;
    private List<AttemptSummaryDTO> attempts;
    private BigDecimal averageScore;
    private Integer totalAttempts;
    private Integer passedAttempts;
}
