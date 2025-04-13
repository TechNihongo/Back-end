package org.example.technihongo.repositories;


import org.example.technihongo.enums.CompletionStatus;
import org.springframework.stereotype.Repository;
import org.example.technihongo.entities.StudentCourseProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentCourseProgressRepository extends JpaRepository<StudentCourseProgress, Integer>{
    Optional<StudentCourseProgress> findByStudent_StudentIdAndCourse_CourseId(Integer studentId, Integer courseId);
    List<StudentCourseProgress> findByStudent_StudentId(Integer studentId);
    Integer countByCourse_CourseIdAndCompletionStatus(Integer courseId, CompletionStatus completionStatus);
    boolean existsByStudentStudentIdAndCourseCourseId(Integer studentId, Integer courseId);
    List<StudentCourseProgress> findByCourse_CourseId(Integer courseId);
    long countByStudent_StudentIdAndCompletionStatus(Integer studentId, CompletionStatus completionStatus);
}
