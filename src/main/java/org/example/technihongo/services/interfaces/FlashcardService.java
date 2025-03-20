package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.FlashcardRequestDTO;
import org.example.technihongo.dto.FlashcardResponseDTO;

import java.util.List;

public interface FlashcardService {
    List<FlashcardResponseDTO> createStudentFlashcards(Integer studentId, Integer flashcardSetId, List<FlashcardRequestDTO> requests);
    List<FlashcardResponseDTO> createSystemFlashcards(Integer userId, Integer flashcardSetId, List<FlashcardRequestDTO> requests);

    FlashcardResponseDTO updateFlashcard(Integer userId, Integer flashcardId, FlashcardRequestDTO request);
    void deleteFlashcard(Integer userId, Integer flashcardId);
    FlashcardResponseDTO getFlashcardById(Integer flashcardId);
}
