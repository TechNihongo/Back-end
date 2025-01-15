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
@Table(name = "[StudentFlashCardSetProgress]")
public class StudentFlashCardSetProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Integer progressId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;


    // ??
    @ManyToOne
    @JoinColumn(name = "set_id", nullable = false)
    private StudentFlashCardSet flashcardSet;

    @Column(name = "completion_percentage", precision = 5, scale = 2)
    private BigDecimal completionPercentage;

    @Column(name = "last_studied")
    private LocalDateTime lastStudied;

    @Column(name = "study_count")
    private Integer studyCount;

    @Column(name = "total_study_time")
    private Integer totalStudyTime;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    
}
