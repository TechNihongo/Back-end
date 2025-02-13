package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.FlashcardRequestDTO;
import org.example.technihongo.dto.FlashcardResponseDTO;

public interface FlashcardService {
    FlashcardResponseDTO createFlashcard(Integer studentId, Integer flashcardSetId, FlashcardRequestDTO request);
    FlashcardResponseDTO updateFlashcard(Integer studentId, Integer flashcardId, FlashcardRequestDTO request);
    void deleteFlashcard(Integer studentId, Integer flashcardId);
    FlashcardResponseDTO getFlashcardById(Integer flashcardId);
}
