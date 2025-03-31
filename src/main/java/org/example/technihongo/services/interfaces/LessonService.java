package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.CreateLessonDTO;
import org.example.technihongo.dto.PageResponseDTO;
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

    PageResponseDTO<Lesson> getLessonListByStudyPlanIdPaginated(Integer studyPlanId, int pageNo, int pageSize, String sortBy, String sortDir, String keyword);
    void checkLessonProgressPrerequisite(Integer studentId, Integer lessonId);
    Integer getCourseIdByLessonId(Integer lessonId);
    void setLessonOrder(Integer studyPlanId, Integer lessonId, Integer newOrder);
}
