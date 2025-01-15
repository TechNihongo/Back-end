package org.example.technihongo.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "QuizAnswerResponse")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizAnswerResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_id")
    private Integer responseId;

    @ManyToOne
    @JoinColumn(name = "attempt_id", nullable = false)
    private StudentQuizAttempt studentQuizAttempt;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuizQuestion quizQuestion;

    @ManyToOne
    @JoinColumn(name = "selected_option_id")
    private QuizAnswerOption selectedOption;

    @Column(name = "text_response")
    private String textResponse;

    @Column(name = "is_correct")
    private boolean isCorrect;

    @Column(name = "points_earned", precision = 5, scale = 2)
    private BigDecimal pointsEarned;

    @Column(name = "response_time")
    private Integer responseTime;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
}
