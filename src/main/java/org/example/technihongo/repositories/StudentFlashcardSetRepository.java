package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentFlashcardSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentFlashcardSetRepository extends JpaRepository<StudentFlashcardSet, Integer> {
    List<StudentFlashcardSet> findByCreatorStudentId(Integer studentId);

    List<StudentFlashcardSet> findByTitleContainingIgnoreCase(String keyword);
    Boolean existsByLearningResource_ResourceId(Integer learningResourceId);
}
