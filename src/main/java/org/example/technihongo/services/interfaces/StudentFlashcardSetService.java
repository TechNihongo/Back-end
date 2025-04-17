package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.*;

import java.util.List;

public interface StudentFlashcardSetService {
    FlashcardSetResponseDTO createFlashcardSet(Integer studentId, FlashcardSetRequestDTO request);
    FlashcardSetResponseDTO updateFlashcardSet(Integer studentId, Integer flashcardSetId, FlashcardSetRequestDTO request);
    void deleteFlashcardSet(Integer studentId, Integer flashcardSetId);
    FlashcardSetViolationResponseDTO setViolatedFlashcardSet(Integer flashcardSetId, Integer violationCount);


    List<FlashcardSetResponseDTO> getFlashcardSetsByStudentId(Integer studentId);

    List<FlashcardSetResponseDTO> getFlashcardSetsByPublicStatus();


    void updateFlashcardOrder(Integer studentId,Integer flashcardSetId, UpdateFlashcardOrderDTO updateFlashcardOrderDTO);

    FlashcardSetResponseDTO updateFlashcardSetVisibility(Integer studentId, Integer flashcardSetId, Boolean isPublic);

    FlashcardSetResponseDTO getAllFlashcardsInSet(Integer studentId ,Integer flashcardSetId);

    List<FlashcardSetResponseDTO> studentFlashcardList(Integer studentId);

    List<FlashcardSetResponseDTO> searchTitle(String keyword);

    FlashcardSetResponseDTO createFlashcardSetFromResource(Integer studentId, CreateFlashcardSetFromResourceDTO request);

    FlashcardSetResponseDTO cloneStudentFlashcardSet(Integer studentId, Integer studentSetIdToClone);

}
