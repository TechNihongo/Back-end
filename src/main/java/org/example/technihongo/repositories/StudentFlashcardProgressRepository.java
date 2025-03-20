package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentFlashcardProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentFlashcardProgressRepository extends JpaRepository<StudentFlashcardProgress, Integer> {
    List<StudentFlashcardProgress> findByStudentStudentIdAndFlashcard_StudentFlashCardSet_StudentSetIdAndStarred(Integer studentId, Integer studentSetId, boolean starred);
    List<StudentFlashcardProgress> findByStudentStudentIdAndFlashcard_SystemFlashCardSet_SystemSetIdAndStarred(Integer studentId, Integer systemSetId, boolean starred);
    Optional<StudentFlashcardProgress> findByStudentStudentIdAndFlashcardFlashCardId(Integer studentId, Integer flashcardId);
}
