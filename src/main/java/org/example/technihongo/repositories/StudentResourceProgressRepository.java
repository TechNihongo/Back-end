package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentResourceProgress;
import org.example.technihongo.enums.CompletionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentResourceProgressRepository extends JpaRepository<StudentResourceProgress, Integer> {
    Boolean existsByLearningResource_ResourceId(Integer learningResourceId);
    Optional<StudentResourceProgress> findByStudent_StudentIdAndLearningResource_ResourceId(Integer studentId, Integer resourceId);
    List<StudentResourceProgress> findByStudent_StudentId(Integer studentId);
    boolean existsByStudentStudentIdAndLearningResourceResourceIdAndCompletionStatus(Integer studentId, Integer resourceId, CompletionStatus status);
}
