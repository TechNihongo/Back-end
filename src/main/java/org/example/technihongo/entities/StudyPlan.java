package org.example.technihongo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "StudyPlan")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_plan_id")
    private Integer studyPlanId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false, referencedColumnName = "course_id")
    private Course course;

    @NotNull
    @Size(max = 50)
    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Size(max = 50)
    @Column(name = "description", length = 50)
    private String description;

    @NotNull
    @Column(name = "hours_per_day")
    private Integer hoursPerDay;

    @Column(name = "is_default")
    private boolean isDefault;

    @Column(name = "is_active")
    private boolean active;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}