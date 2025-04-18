package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentFlashcardSetProgress;
import org.example.technihongo.enums.CompletionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentFlashcardSetProgressRepository extends JpaRepository<StudentFlashcardSetProgress, Integer> {
    List<StudentFlashcardSetProgress> findByStudentStudentId(Integer studentId);
    Optional<StudentFlashcardSetProgress> findByStudentStudentIdAndStudentFlashcardSet_StudentSetId(Integer studentId, Integer studentSetId);
    Optional<StudentFlashcardSetProgress> findByStudentStudentIdAndSystemFlashcardSet_SystemSetId(Integer studentId, Integer systemSetId);
    boolean existsByStudentStudentIdAndSystemFlashcardSetSystemSetIdAndCompletionStatus(Integer studentId, Integer systemSetId, CompletionStatus status);
    long countByStudentStudentIdAndStudentFlashcardSetNotNullAndCompletionStatus(Integer studentId, CompletionStatus status);
}
