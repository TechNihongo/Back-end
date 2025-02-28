package org.example.technihongo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "StudentStudyPlan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentStudyPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_plan_id")
    private Integer studentPlanId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false, referencedColumnName = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "study_plan_id", nullable = false, referencedColumnName = "study_plan_id")
    private StudyPlan studyPlan;

    @ManyToOne
    @JoinColumn(name = "previous_plan_id", referencedColumnName = "study_plan_id")
    private StudyPlan previousPlan;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "switch_date")
    private LocalDateTime switchDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
