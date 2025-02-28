package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.CreateLessonDTO;
import org.example.technihongo.dto.UpdateLessonDTO;
import org.example.technihongo.dto.UpdateLessonOrderDTO;
import org.example.technihongo.entities.Lesson;

import java.util.List;
import java.util.Optional;

public interface LessonService {
    Optional<Lesson> getLessonById(Integer lessonId);
    List<Lesson> getLessonListByStudyPlanId(Integer studyPlanId);
    Lesson createLesson(CreateLessonDTO createLessonDTO);
    void updateLesson(Integer lessonId, UpdateLessonDTO updateLessonDTO);
    void updateLessonOrder(Integer studyPlanId, UpdateLessonOrderDTO updateLessonOrderDTO);
}
