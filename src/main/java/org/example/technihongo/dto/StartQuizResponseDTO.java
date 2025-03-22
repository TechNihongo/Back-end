package org.example.technihongo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StartQuizResponseDTO {
    private Integer attemptId;
    private Integer quizId;
    private String title;
    private Integer totalQuestions;
    private Integer attemptNumber;
    private LocalDateTime startTime;
    private Boolean resuming;
    private Long remainingTimeInSeconds;
}
