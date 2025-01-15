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
@Table(name = "[StudentLessonProgress]")
public class StudentLessonProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Integer progressId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Column(name = "completion_percentage", precision = 5, scale = 2)
    private BigDecimal completionPercentage;

    @Column(name = "completion_status")
    private String completionStatus;

    @Column(name = "last_studied")
    private LocalDateTime lastStudied;

    @Column(name = "completed_resources")
    private Integer completedResources;

    @Column(name = "total_study_time")
    private Integer totalStudyTime;

    @Column(name = "highest_quiz_score", precision = 5, scale = 2)
    private BigDecimal highestQuizScore;

    @Column(name = "notes")
    private String notes;
    
}
