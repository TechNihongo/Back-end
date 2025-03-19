package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentFlashcardProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentFlashcardProgressRepository extends JpaRepository<StudentFlashcardProgress, Integer> {
    List<StudentFlashcardProgress> findByStudentStudentIdAndFlashcard_StudentFlashCardSet_StudentSetId(Integer studentId, Integer studentSetId);
    List<StudentFlashcardProgress> findByStudentStudentIdAndFlashcard_SystemFlashCardSet_SystemSetId(Integer studentId, Integer systemSetId);
    Optional<StudentFlashcardProgress> findByStudentStudentIdAndFlashcardFlashCardId(Integer studentId, Integer flashcardId);
}
