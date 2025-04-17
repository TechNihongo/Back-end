package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentViolation;
import org.example.technihongo.enums.ViolationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StudentViolationRepository extends JpaRepository<StudentViolation, Integer> {
    @Query("SELECT sv FROM StudentViolation sv " +
            "WHERE (:classifyBy = 'flashcardset' AND sv.studentFlashcardSet IS NOT NULL " +
            "       OR :classifyBy = 'rating' AND sv.studentCourseRating IS NOT NULL) " +
            "AND (:status IS NULL OR sv.status = :status)")
    Page<StudentViolation> findByClassifyByAndStatus(
            @Param("classifyBy") String classifyBy,
            @Param("status") ViolationStatus status,
            Pageable pageable);

    StudentViolation findByViolationId(Integer violationId);

    @Query("SELECT v FROM StudentViolation v " +
            "WHERE v.studentFlashcardSet.studentSetId = :studentSetId " +
            "AND v.status = :status " +
            "AND v.violationId != :violationId")
    List<StudentViolation> findByStudentFlashcardSetStudentSetIdAndStatusAndViolationIdNot(
            @Param("studentSetId") Integer studentSetId,
            @Param("status") ViolationStatus status,
            @Param("violationId") Integer violationId);

    @Query("SELECT v FROM StudentViolation v " +
            "WHERE v.studentCourseRating.ratingId = :ratingId " +
            "AND v.status = :status " +
            "AND v.violationId != :violationId")
    List<StudentViolation> findByStudentCourseRatingRatingIdAndStatusAndViolationIdNot(
            @Param("ratingId") Integer ratingId,
            @Param("status") ViolationStatus status,
            @Param("violationId") Integer violationId);

    @Query("SELECT v FROM StudentViolation v " +
            "WHERE v.studentFlashcardSet.studentSetId = :studentSetId " +
            "AND (:status IS NULL OR v.status = :status)")
    Page<StudentViolation> findByStudentFlashcardSetId(
            @Param("studentSetId") Integer studentSetId,
            @Param("status") ViolationStatus status,
            Pageable pageable);

    @Query("SELECT v FROM StudentViolation v " +
            "WHERE v.studentCourseRating.ratingId = :ratingId " +
            "AND (:status IS NULL OR v.status = :status)")
    Page<StudentViolation> findByStudentCourseRatingId(
            @Param("ratingId") Integer ratingId,
            @Param("status") ViolationStatus status,
            Pageable pageable);

    @Query("SELECT COUNT(v) FROM StudentViolation v " +
            "WHERE v.studentFlashcardSet.studentSetId = :studentSetId")
    long countByStudentFlashcardSetId(@Param("studentSetId") Integer studentSetId);

    @Query("SELECT COUNT(v) FROM StudentViolation v " +
            "WHERE v.studentCourseRating.ratingId = :ratingId")
    long countByStudentCourseRatingId(@Param("ratingId") Integer ratingId);

    boolean existsByReportedByUserIdAndStudentFlashcardSetStudentSetId(Integer reportedBy, Integer contentId);

    List<StudentViolation> findByStatusAndViolationHandledAtBefore(ViolationStatus violationStatus, LocalDateTime oneDayAgo);

    StudentViolation findByStudentFlashcardSetStudentSetIdAndStatus(Integer flashcardSetId, ViolationStatus violationStatus);
}
