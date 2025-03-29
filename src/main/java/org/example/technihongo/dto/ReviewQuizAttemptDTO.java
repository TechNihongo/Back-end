package org.example.technihongo.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewQuizAttemptDTO {
    private Integer attemptId;
    private Integer quizId;
    private String quizTitle;
    private BigDecimal score;
    private Boolean isPassed;
    private LocalTime timeTaken;
    private Boolean isCompleted;
    private Integer attemptNumber;
    private LocalDateTime dateTaken;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Integer incorrectAnswers;
    private Integer unansweredQuestions;
    private List<AnswerReviewDTO> answers;
}
