package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.CreateLessonResourceDTO;
import org.example.technihongo.dto.PageResponseDTO;
import org.example.technihongo.dto.UpdateLessonResourceDTO;
import org.example.technihongo.dto.UpdateLessonResourceOrderDTO;
import org.example.technihongo.entities.LessonResource;

import java.util.List;

public interface LessonResourceService {
    List<LessonResource> getLessonResourceListByLessonId(Integer lessonId);
    List<LessonResource> getActiveLessonResourceListByLessonId(Integer lessonId);
    LessonResource getLessonResourceById(Integer lessonResourceId);
    LessonResource getActiveLessonResourceById(Integer lessonResourceId);
    LessonResource createLessonResource(CreateLessonResourceDTO createLessonResourceDTO);
    void updateLessonResource(Integer lessonResourceId, UpdateLessonResourceDTO updateLessonResourceDTO);
    void updateLessonResourceOrder(Integer lessonId, UpdateLessonResourceOrderDTO updateLessonResourceOrderDTO);
    void deleteLessonResource(Integer lessonResourceId);

    PageResponseDTO<LessonResource> getLessonResourcesByDefaultStudyPlanPaginated(Integer studyPlanId, String keyword, String type, int pageNo, int pageSize, String sortBy, String sortDir);
    void setLessonResourceOrder(Integer lessonId, Integer lessonResourceId, Integer newOrder);
}
