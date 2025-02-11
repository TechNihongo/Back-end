package org.example.technihongo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "StudentQuizAttempt")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentQuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_id")
    private Integer attemptId;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false, referencedColumnName = "quiz_id")
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false, referencedColumnName = "student_id")
    private Student student;

    @Column(name = "score", precision = 5, scale = 2)
    private BigDecimal score;

    @Column(name = "is_passed")
    private Boolean isPassed;

    @Column(name = "time_taken")
    private Integer timeTaken;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "attempt_number")
    private Integer attemptNumber;

    @CreationTimestamp
    @Column(name = "date_taken", updatable = false)
    private LocalDateTime dateTaken;
}
