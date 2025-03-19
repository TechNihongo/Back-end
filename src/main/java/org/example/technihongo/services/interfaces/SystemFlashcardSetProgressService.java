package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.FlashcardSetProgressDTO;

import java.util.List;

public interface SystemFlashcardSetProgressService {
    List<FlashcardSetProgressDTO> getAllSystemSetProgress(Integer studentId);
    FlashcardSetProgressDTO getSystemSetProgress(Integer studentId, Integer systemSetId);

}
