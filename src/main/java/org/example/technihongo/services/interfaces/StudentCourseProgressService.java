package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.CourseStatisticsDTO;
import org.example.technihongo.entities.StudentCourseProgress;

import java.util.List;

public interface StudentCourseProgressService {
    StudentCourseProgress getStudentCourseProgress(Integer studentId, Integer courseId);
    List<StudentCourseProgress> getAllStudentCourseProgress(Integer studentId);
    CourseStatisticsDTO viewCourseStatistics(Integer courseId);
    void enrollCourse(Integer studentId, Integer courseId);
    void trackStudentCourseProgress(Integer studentId, Integer courseId, Integer currentLessonId);
}
