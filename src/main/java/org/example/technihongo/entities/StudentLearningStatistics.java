package org.example.technihongo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "StudentLearningStatistics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentLearningStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "learning_stat_id")
    private Integer learningStatId;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false, referencedColumnName = "student_id", unique = true)
    private Student student;

    @Column(name = "total_study_time")
    private Integer totalStudyTime = 0;

    @Column(name = "total_completed_courses")
    private Integer totalCompletedCourses = 0;

    @Column(name = "total_completed_lessons")
    private Integer totalCompletedLessons = 0;

    @Column(name = "total_completed_quizzes")
    private Integer totalCompletedQuizzes = 0;

    @Column(name = "active_days_count")
    private Integer maxDaysCount = 0;

    @Column(name = "max_days_streak")
    private Integer activeDaysStreak = 0;

    @Column(name = "total_achievements_unlocked")
    private Integer totalAchievementsUnlocked = 0;

    @Column(name = "last_study_date")
    private LocalDateTime lastStudyDate;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
