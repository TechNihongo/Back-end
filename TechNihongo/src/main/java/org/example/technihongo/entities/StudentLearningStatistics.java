package org.example.technihongo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "[StudentLearningStatistics]")
public class StudentLearningStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stat_id")
    private Integer statId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "total_study_time")
    private Integer totalStudyTime;

    @Column(name = "total_completed_courses")
    private Integer totalCompletedCourses;

    @Column(name = "total_completed_lessons")
    private Integer totalCompletedLessons;

    @Column(name = "total_completed_quizzes")
    private Integer totalCompletedQuizzes;

    @Column(name = "active_days_count")
    private Integer activeDaysCount;

    @Column(name = "total_achievements_unlocked")
    private Integer totalAchievementsUnlocked;

    @Column(name = "last_study_date")
    private LocalDateTime lastStudyDate;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
