package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentCourseProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface StudentCourseProgressRepository extends JpaRepository<StudentCourseProgress, Integer> {
    boolean existsByStudentStudentIdAndCourseCourseId(Integer studentId, Integer courseId);
}
