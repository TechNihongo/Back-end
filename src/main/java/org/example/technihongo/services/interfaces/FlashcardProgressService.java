package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.FlashcardProgressDTO;

import java.util.List;

public interface FlashcardProgressService {
    List<FlashcardProgressDTO> getFlashcardProgress(Integer studentId, Integer setId, boolean isSystemSet);
    void updateFlashcardProgress(Integer studentId, Integer flashcardId, Boolean isLearned, Boolean starred);
}

