package org.example.technihongo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "StudentFavorite")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Integer favoriteId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false, referencedColumnName = "student_id")
    private Student student;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "lesson_resource_id", nullable = false, referencedColumnName = "lesson_resource_id")
    private LessonResource lessonResource;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}