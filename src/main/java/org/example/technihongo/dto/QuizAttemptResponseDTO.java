package org.example.technihongo.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttemptResponseDTO {
    private Integer attemptId;
    private Integer quizId;
    private BigDecimal score;
    private boolean isPassed;
    private Integer timeTaken;
    private boolean isCompleted;
    private Integer attemptNumber;
    private LocalDateTime dateTaken;

}
