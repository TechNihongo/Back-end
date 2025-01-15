package org.example.technihongo.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "[PathCourse]")
public class PathCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "path_course_id")
    private Integer pathCourseId;

    @ManyToOne
    @JoinColumn(name = "path_id")
    private LearningPath learningPath;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "course_order")
    private Integer courseOrder;
}
