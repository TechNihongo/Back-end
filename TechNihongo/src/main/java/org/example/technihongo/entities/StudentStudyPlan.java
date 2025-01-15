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
@Table(name = "[StudentStudyPlan]")
public class StudentStudyPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_plan_id")
    private Integer studentPlanId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "course_plan_id")
    private CourseStudyPlan courseStudyPlan;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "previous_plan_id")
    private CourseStudyPlan previousPlan;

    @Column(name = "switch_date")
    private LocalDateTime switchDate;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    
}
