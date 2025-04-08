package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.FlashcardRequestDTO;
import org.example.technihongo.dto.FlashcardResponseDTO;
import org.example.technihongo.dto.PageResponseDTO;

import java.util.List;

public interface FlashcardService {
    PageResponseDTO<FlashcardResponseDTO> getStudentFlashcards(Integer studentId, Integer flashcardSetId,
                                                               int pageNo, int pageSize, String sortBy, String sortDir);
    PageResponseDTO<FlashcardResponseDTO> getSystemFlashcards(Integer userId, Integer flashcardSetId,
                                                              int pageNo, int pageSize, String sortBy, String sortDir);

    List<FlashcardResponseDTO> createStudentFlashcards(Integer studentId, Integer flashcardSetId, List<FlashcardRequestDTO> requests);
    List<FlashcardResponseDTO> createSystemFlashcards(Integer userId, Integer flashcardSetId, List<FlashcardRequestDTO> requests);



    FlashcardResponseDTO updateFlashcard(Integer userId, Integer studentId, Integer flashcardId, FlashcardRequestDTO request);
    void deleteFlashcard(Integer userId, Integer studentId , Integer flashcardId);
    FlashcardResponseDTO getFlashcardById(Integer flashcardId);
}