package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.CreateLessonResourceDTO;
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
}
