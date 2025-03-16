package org.example.technihongo.entities;


import jakarta.persistence.*;
import lombok.*;
import org.example.technihongo.enums.CompletionStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "StudentResourceProgress")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResourceProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Integer progressId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false, referencedColumnName = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false, referencedColumnName = "resource_id")
    private LearningResource learningResource;

    @Enumerated(EnumType.STRING)
    @Column(name = "completion_status", length = 20)
    private CompletionStatus completionStatus;

    @Column(name = "last_studied")
    private LocalDateTime lastStudied;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
