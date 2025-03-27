package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentViolation;
import org.example.technihongo.enums.ViolationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
