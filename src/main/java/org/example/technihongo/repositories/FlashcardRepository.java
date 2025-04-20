package org.example.technihongo.repositories;

import jakarta.transaction.Transactional;
import org.example.technihongo.entities.Flashcard;
import org.example.technihongo.entities.StudentFlashcardSet;
import org.example.technihongo.entities.SystemFlashcardSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Integer> {
    Page<Flashcard> findByStudentFlashCardSetStudentSetId(Integer studentSetId, Pageable pageable);
    Page<Flashcard> findBySystemFlashCardSetSystemSetId(Integer systemSetId, Pageable pageable);

    List<Flashcard> findBySystemFlashCardSet_SystemSetId(Integer systemSetId);

    List<Flashcard> findByStudentFlashCardSet_StudentSetId(Integer studentSetId);

    @Query("SELECT MAX(f.cardOrder) FROM Flashcard f WHERE f.studentFlashCardSet = :flashcardSet")
    Integer findMaxVocabOrderByStudentFlashCardSet(@Param("flashcardSet") StudentFlashcardSet flashcardSet);

    @Query("select MAX(f.cardOrder) FROM Flashcard f WHERE f.systemFlashCardSet = :flashcardSet")
    Integer findMaxVocabOrderBySystemFlashCardSet(@Param("flashcardSet") SystemFlashcardSet flashcardSet);

    @Query("SELECT f FROM Flashcard f WHERE f.systemFlashCardSet.systemSetId = :setId ORDER BY f.cardOrder ASC, f.flashCardId ASC")
    List<Flashcard> findTopBySystemFlashCardSet_SystemSetIdOrderByCardOrderAsc(@Param("setId") Integer setId);

    @Query("SELECT f FROM Flashcard f WHERE f.studentFlashCardSet.studentSetId = :setId ORDER BY f.cardOrder ASC, f.flashCardId ASC")
    List<Flashcard> findTopByStudentFlashCardSet_StudentSetIdOrderByCardOrderAsc(@Param("setId") Integer setId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM flashcard WHERE flashcard_id = :flashcardId", nativeQuery = true)
    void deleteByFlashcardIdNative(@Param("flashcardId") Integer flashcardId);


}