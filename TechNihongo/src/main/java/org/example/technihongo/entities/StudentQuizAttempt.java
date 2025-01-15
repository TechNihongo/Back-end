package org.example.technihongo.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "StudentQuizAttempt")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentQuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_id")
    private Integer attemptId;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "score", precision = 5, scale = 2)
    private BigDecimal score;

    @Column(name = "passing_score", precision = 5, scale = 2)
    private BigDecimal passingScore;

    @Column(name = "time_taken")
    private Integer timeTaken;

    @Column(name = "is_completed")
    private boolean isCompleted;

    @Column(name = "attempt_number")
    private Integer attemptNumber;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
}
