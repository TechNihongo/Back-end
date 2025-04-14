package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentCourseRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentCourseRatingRepository extends JpaRepository<StudentCourseRating, Integer> {
    boolean existsByStudentStudentIdAndCourseCourseId(Integer studentId, Integer courseId);
    List<StudentCourseRating> findByCourseCourseId(Integer courseId);
    Page<StudentCourseRating> findByCourse_CourseId(Integer courseId, Pageable pageable);
    Optional<StudentCourseRating> findByStudentStudentIdAndCourseCourseId(Integer studentId, Integer courseId);
}
