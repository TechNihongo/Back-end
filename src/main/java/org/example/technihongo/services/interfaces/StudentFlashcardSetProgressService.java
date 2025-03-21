package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.FlashcardSetProgressDTO;

import java.util.List;

public interface StudentFlashcardSetProgressService {
    List<FlashcardSetProgressDTO> getAllStudentAndSystemSetProgress(Integer studentId);
    FlashcardSetProgressDTO getStudentOrSystemSetProgress(Integer studentId, Integer studentSetId, boolean isSystemSet);
}
