package org.example.technihongo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "StudentDailyLearningLog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDailyLearningLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "student_id", nullable = false, referencedColumnName = "student_id")
    private Student student;

    @Column(name = "log_date")
    private LocalDate logDate;

    @Column(name = "study_time")
    private Integer studyTime;

    @Column(name = "completed_lessons")
    private Integer completedLessons;

    @Column(name = "completed_quizzes")
    private Integer completedQuizzes;

    @Column(name = "completed_resources")
    private Integer completedResources;

    @Column(name = "completed_flashcard_sets")
    private Integer completedFlashcardSets;

    @Column(name = "daily_goal_achieved")
    private Integer dailyGoalAchieved;

    @Column(name = "study_streak")
    private Integer streak;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
