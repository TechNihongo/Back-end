package org.example.technihongo.entities;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "[LessonResource]")
public class LessonResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_resource_id")
    private Integer lessonResourceId;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Column(name = "type")
    private String type;

    @Column(name = "type_order")
    private Integer typeOrder;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    private LearningResource learningResource;

    @ManyToOne
    @JoinColumn(name = "set_id")
    private SystemFlashCardSet systemFlashCardSet;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}
