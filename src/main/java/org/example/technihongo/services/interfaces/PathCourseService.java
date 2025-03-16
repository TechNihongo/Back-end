package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.CreatePathCourseDTO;
import org.example.technihongo.dto.UpdatePathCourseOrderDTO;
import org.example.technihongo.entities.PathCourse;

import java.util.List;

public interface PathCourseService {
    List<PathCourse> getPathCoursesByLearningPathId(Integer pathId);
    PathCourse getPathCourseById(Integer pathCourseId);
    PathCourse createPathCourse(CreatePathCourseDTO createPathCourseDTO);
    void updatePathCourseOrder(Integer pathId, UpdatePathCourseOrderDTO updatePathCourseOrderDTO);
    void deletePathCourse(Integer pathCourseId);
    void setPathCourseOrder(Integer pathId, Integer pathCourseId, Integer newOrder);
}
