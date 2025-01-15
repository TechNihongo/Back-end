package org.example.technihongo.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Quiz")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "quiz_id")
    private Integer quizId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "quiz_type")
    private String quizType;

    @ManyToOne
    @JoinColumn(name = "domain_id")
    private Domain domain;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "difficulty_level_id", nullable = false)
    private DifficultyLevel difficultyLevel;

    @Column(name = "time_limit")
    private Integer timeLimit;

    @Column(name = "passing_score", precision = 5, scale = 2)
    private BigDecimal passingScore;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    
}
