package org.example.technihongo.dto;

import lombok.*;
import org.example.technihongo.entities.DifficultyLevel;
import org.example.technihongo.entities.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizDTO {
    private Integer quizId;
    private String title;
    private String description;
    private User creator;
    private DifficultyLevel difficultyLevel;
    private Integer totalQuestions;
    private BigDecimal passingScore;
    private boolean isPublic;
    private boolean isDeleted;
    private boolean isPremium;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean hasAttempt;
}
