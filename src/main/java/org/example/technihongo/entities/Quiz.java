package org.example.technihongo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Quiz")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Integer quizId;

    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(name = "description", length = 255)
    private String description;

//    @ManyToOne
//    @JoinColumn(name = "domain_id", nullable = false, referencedColumnName = "domain_id")
//    private Domain domain;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false, referencedColumnName = "user_id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "difficulty_level_id", nullable = false, referencedColumnName = "level_id")
    private DifficultyLevel difficultyLevel;

    @Column(name = "total_questions")
    private Integer totalQuestions;

    @Column(name = "passing_score", precision = 5, scale = 2)
    private BigDecimal passingScore;

    @Column(name = "is_public")
    private boolean isPublic = false;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
