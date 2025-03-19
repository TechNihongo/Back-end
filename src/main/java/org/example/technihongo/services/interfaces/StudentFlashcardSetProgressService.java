package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.FlashcardSetProgressDTO;

import java.util.List;

public interface StudentFlashcardSetProgressService {
    List<FlashcardSetProgressDTO> getAllStudentSetProgress(Integer studentId);
    FlashcardSetProgressDTO getStudentSetProgress(Integer studentId, Integer studentSetId);
}
