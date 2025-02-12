package org.example.technihongo.services.interfaces;

import org.example.technihongo.entities.Lesson;

import java.util.List;
import java.util.Optional;

public interface LessonService {
    Optional<Lesson> getLessonById(Integer lessonId);
    List<Lesson> getLessonListByCourseStudyPlanId(Integer coursePlanId);
}
