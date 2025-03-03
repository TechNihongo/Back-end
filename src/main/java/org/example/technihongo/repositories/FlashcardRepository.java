package org.example.technihongo.repositories;

import org.example.technihongo.entities.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Integer> {
    List<Flashcard> findByStudentFlashCardSetStudentSetId(Integer studentSetId);
    List<Flashcard> findBySystemFlashCardSetSystemSetId(Integer systemSetId);
    List<Flashcard> findByStudentFlashCardSet_StudentSetId(Integer studentSetId);
}
