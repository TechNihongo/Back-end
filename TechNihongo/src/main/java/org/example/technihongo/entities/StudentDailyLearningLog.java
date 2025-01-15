package org.example.technihongo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "[StudentDailyLearningLog]")
public class StudentDailyLearningLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "log_date")
    private LocalDateTime logDate;

    @Column(name = "study_time")
    private Integer studyTime;

    @Column(name = "completed_lessons")
    private Integer completedLessons;

    @Column(name = "completed_courses")
    private Integer completedCourses;

    @Column(name = "quiz_attempts")
    private Integer quizAttempts;

    @Column(name = "points_earned", precision = 5, scale = 2)
    private BigDecimal pointsEarned;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
}
