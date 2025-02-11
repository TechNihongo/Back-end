package org.example.technihongo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
@Entity
@Table(name = "PathCourse")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PathCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "path_course_id")
    private Integer pathCourseId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "path_id", nullable = false, referencedColumnName = "path_id")
    private LearningPath learningPath;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false, referencedColumnName = "course_id")
    private Course course;

    @Column(name = "course_order")
    private Integer courseOrder;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
}