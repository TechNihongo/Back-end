package org.example.technihongo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "LessonResource")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_resource_id")
    private Integer lessonResourceId;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "lesson_id", referencedColumnName = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(name = "type")
    private String type;

    @Column(name = "type_order")
    private Integer typeOrder;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "resource_id", referencedColumnName = "resource_id", nullable = false)
    private LearningResource learningResource;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "system_set_id", referencedColumnName = "system_set_id", nullable = false)
    private SystemFlashcardSet systemFlashCardSet;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "quiz_id", referencedColumnName = "quiz_id", nullable = false)
    private Quiz quiz;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
