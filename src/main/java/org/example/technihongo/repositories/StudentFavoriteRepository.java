package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentFavorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentFavoriteRepository extends JpaRepository<StudentFavorite, Integer>{
    Boolean existsByLearningResource_ResourceId(Integer learningResourceId);
    boolean existsByStudent_StudentIdAndLearningResource_ResourceId(Integer studentId, Integer learningResourceId);
    @Query("SELECT sf FROM StudentFavorite sf WHERE sf.student.studentId = :studentId " +
            "AND sf.learningResource.isPublic = :isPublic")
    Page<StudentFavorite> findByStudent_StudentIdAndLearningResource_IsPublic(
            @Param("studentId") Integer studentId,
            @Param("isPublic") boolean isPublic,
            Pageable pageable);

    @Query("SELECT sf FROM StudentFavorite sf WHERE sf.student.studentId = :studentId " +
            "AND sf.learningResource.isPublic = :isPublic AND sf.learningResource.isPremium = :isPremium")
    Page<StudentFavorite> findByStudent_StudentIdAndLearningResource_IsPublicAndLearningResource_IsPremium(
            @Param("studentId") Integer studentId,
            @Param("isPublic") boolean isPublic,
            @Param("isPremium") boolean isPremium,
            Pageable pageable);

    Optional<StudentFavorite> findByStudent_StudentIdAndLearningResource_ResourceId(Integer studentId, Integer learningResourceId);
}
