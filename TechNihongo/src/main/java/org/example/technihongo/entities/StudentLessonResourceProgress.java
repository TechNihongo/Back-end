package org.example.technihongo.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "[StudentLessonResourceProgress]")
public class StudentLessonResourceProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Integer progressId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "lesson_resource_id")
    private LessonResource lessonResource;

    @Column(name = "completion_status")
    private String completionStatus;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "last_accessed")
    private LocalDateTime lastAccessed;

    @Column(name = "total_time_spent")
    private Integer totalTimeSpent;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    
}
