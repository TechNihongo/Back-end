package org.example.technihongo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Lesson")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Integer lessonId;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "study_plan_id", nullable = false, referencedColumnName = "study_plan_id")
    private StudyPlan studyPlan;

    @Column(name = "title")
    private String title;

    @Column(name = "lesson_order")
    private Integer lessonOrder;

//    @Column(name = "is_prerequisite")
//    private boolean isPrerequisite;

//    @Column(name = "min_completion_percentage", precision = 5, scale = 2)
//    private BigDecimal minCompletionPercentage;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
