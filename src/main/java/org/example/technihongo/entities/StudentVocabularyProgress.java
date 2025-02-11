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
@Table(name = "[StudentVocabularyProgress]")
public class StudentVocabularyProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Integer progressId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "vocab_id")
    private FlashCard vocab;

    @Column(name = "is_learned")
    private boolean isLearned;

    @Column(name = "last_studied")
    private LocalDateTime lastStudied;

    @Column(name = "study_count")
    private Integer studyCount;

    @Column(name = "total_study_time")
    private Integer totalStudyTime;


    // Note the word you need to review again ^^
    @Column(name = "starred")
    private boolean starred;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    
}
