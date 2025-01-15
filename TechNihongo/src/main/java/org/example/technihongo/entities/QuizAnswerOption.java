package org.example.technihongo.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "QuizAnswerOption")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizAnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Integer optionId;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuizQuestion quizQuestion; 

    @Column(name = "option_text")
    private String optionText;

    @Column(name = "is_correct")
    private boolean isCorrect;

    @Column(name = "explanation")
    private String explanation;

    @Column(name = "option_order")
    private Integer optionOrder;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
}
