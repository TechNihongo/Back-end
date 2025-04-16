package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentCourseRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentCourseRatingRepository extends JpaRepository<StudentCourseRating, Integer> {
//    boolean existsByStudentStudentIdAndCourseCourseId(Integer studentId, Integer courseId);
//    List<StudentCourseRating> findByCourseCourseId(Integer courseId);
//    Page<StudentCourseRating> findByCourse_CourseId(Integer courseId, Pageable pageable);
//    Optional<StudentCourseRating> findByStudentStudentIdAndCourseCourseId(Integer studentId, Integer courseId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM StudentCourseRating r " +
            "WHERE r.student.studentId = :studentId " +
            "AND r.course.courseId = :courseId " +
            "AND r.isDeleted = false")
    boolean existsByStudentStudentIdAndCourseCourseId(
            @Param("studentId") Integer studentId,
            @Param("courseId") Integer courseId
    );

    @Query("SELECT r FROM StudentCourseRating r " +
            "WHERE r.course.courseId = :courseId " +
            "AND r.isDeleted = false")
    List<StudentCourseRating> findByCourseCourseId(@Param("courseId") Integer courseId);

    @Query("SELECT r FROM StudentCourseRating r " +
            "WHERE r.course.courseId = :courseId " +
            "AND r.isDeleted = false")
    Page<StudentCourseRating> findByCourse_CourseId(
            @Param("courseId") Integer courseId,
            Pageable pageable
    );

    @Query("SELECT r FROM StudentCourseRating r " +
            "WHERE r.student.studentId = :studentId " +
            "AND r.course.courseId = :courseId " +
            "AND r.isDeleted = false")
    Optional<StudentCourseRating> findByStudentStudentIdAndCourseCourseId(
            @Param("studentId") Integer studentId,
            @Param("courseId") Integer courseId
    );
}
