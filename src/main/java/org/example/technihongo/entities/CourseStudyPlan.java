package org.example.technihongo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "CourseStudyPlan")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseStudyPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_plan_id")
    private Integer coursePlanId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false, referencedColumnName = "course_id")
    private Course course;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "study_plan_id", nullable = false, referencedColumnName = "study_plan_id")
    private StudyPlan studyPlan;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}