package org.example.technihongo.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "QuizQuestion")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer questionId;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "question_text")
    private String questionText;

    @Column(name = "question_type")
    private String questionType;

    @Column(name = "explanation")
    private String explanation;

    @Column(name = "points")
    private Integer points;

    @Column(name = "question_order")
    private Integer questionOrder;

    @Column(name = "is_required")
    private boolean isRequired;

    @Column(name = "media_url")
    private String mediaUrl;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    
}
