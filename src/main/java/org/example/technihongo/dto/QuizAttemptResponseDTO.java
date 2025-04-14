package org.example.technihongo.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private LocalTime timeTaken;
    private boolean isCompleted;
    private Integer attemptNumber;
    private LocalDateTime dateTaken;
    private Integer remainingAttempts;
    private Long remainingWaitTime;

}
