package org.example.technihongo.dto;

import lombok.Builder;
import lombok.Data;
import org.example.technihongo.entities.LearningResource;
import org.example.technihongo.entities.Lesson;
import org.example.technihongo.entities.Quiz;
import org.example.technihongo.entities.SystemFlashcardSet;

import java.time.LocalDateTime;

@Data
@Builder
public class LessonResourceDTO {
    private Integer lessonResourceId;
    private Lesson lesson;
    private String type;
    private Integer typeOrder;
    private LearningResource learningResource;
    private SystemFlashcardSet systemFlashCardSet;
    private Quiz quiz;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isProgressCompleted;
}
