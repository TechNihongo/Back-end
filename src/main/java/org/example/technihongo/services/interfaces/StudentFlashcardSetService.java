package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.CreateFlashcardSetFromResourceDTO;
import org.example.technihongo.dto.FlashcardSetRequestDTO;
import org.example.technihongo.dto.FlashcardSetResponseDTO;

import java.util.List;

public interface StudentFlashcardSetService {
    FlashcardSetResponseDTO createFlashcardSet(Integer studentId, FlashcardSetRequestDTO request);
    FlashcardSetResponseDTO updateFlashcardSet(Integer studentId, Integer flashcardSetId, FlashcardSetRequestDTO request);
    void deleteFlashcardSet(Integer studentId, Integer flashcardSetId);
    FlashcardSetResponseDTO getFlashcardSetById(Integer flashcardSetId);

    FlashcardSetResponseDTO updateFlashcardSetVisibility(Integer studentId, Integer flashcardSetId, Boolean isPublic);

    FlashcardSetResponseDTO getAllFlashcardsInSet(Integer studentId ,Integer flashcardSetId);

    List<FlashcardSetResponseDTO> studentFlashcardList(Integer studentId);

    List<FlashcardSetResponseDTO> searchTitle(String keyword);

    FlashcardSetResponseDTO createFlashcardSetFromResource(Integer studentId, CreateFlashcardSetFromResourceDTO request);

}
