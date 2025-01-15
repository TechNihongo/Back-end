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
@Table(name = "[Lesson]")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Integer lessonId;
    

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private StudyPlan studyPlan;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "lesson_order")
    private Integer lessonOrder;
    
    @Column(name = "is_prerequisite")
    private boolean isPrerequisite;

    @Column(name = "min_completion_percentage", precision = 5, scale = 2)
    private BigDecimal minCompletionPercentage;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
