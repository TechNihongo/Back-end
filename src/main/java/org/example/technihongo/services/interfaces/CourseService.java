package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.CoursePublicDTO;
import org.example.technihongo.dto.CreateCourseDTO;
import org.example.technihongo.dto.UpdateCourseDTO;
import org.example.technihongo.entities.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> courseList();
    List<CoursePublicDTO> getPublicCourses();
    Optional<Course> getCourseById (Integer courseId);
    Optional<CoursePublicDTO> getPublicCourseById (Integer courseId);
    Course createCourse(Integer creatorId, CreateCourseDTO createCourseDTO);
    void updateCourse(Integer courseId, UpdateCourseDTO updateCourseDTO);
    List<Course> searchCourseByTitle(String keyword);
    List<Course> getListCoursesByCreatorId(Integer creatorId);
}
