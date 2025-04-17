package org.example.technihongo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.technihongo.enums.ViolationStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "StudentViolation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentViolation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "violation_id")
    private Integer violationId;

    @ManyToOne
    @JoinColumn(name = "student_set_id", nullable = false, referencedColumnName = "student_set_id")
    private StudentFlashcardSet studentFlashcardSet;

    @ManyToOne
    @JoinColumn(name = "rating_id", nullable = false, referencedColumnName = "rating_id")
    private StudentCourseRating studentCourseRating;

    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @Column(name = "action_taken", length = 100)
    private String actionTaken;

    @ManyToOne
    @JoinColumn(name = "reported_by", nullable = false, referencedColumnName = "user_id")
    private User reportedBy;

    @ManyToOne
    @JoinColumn(name = "handled_by", referencedColumnName = "user_id")
    private User handledBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private ViolationStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "violation_handled_at")
    private LocalDateTime violationHandledAt;
}

