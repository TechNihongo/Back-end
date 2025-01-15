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
@Table(name = "[StudentAchievementProgress]")
public class StudentCourseProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Integer progressId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "completion_percentage", precision = 5, scale = 2)
    private BigDecimal completionPercentage;

    @Column(name = "completion_status")
    private String completionStatus;

    @ManyToOne
    @JoinColumn(name = "current_lesson_id")
    private Lesson currentLesson;

    @Column(name = "completed_lessons")
    private Integer completedLessons;

    @Column(name = "total_study_time")
    private Integer totalStudyTime;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    
}
