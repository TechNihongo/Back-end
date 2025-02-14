package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.dto.FlashcardRequestDTO;
import org.example.technihongo.dto.FlashcardResponseDTO;
import org.example.technihongo.entities.Flashcard;
import org.example.technihongo.entities.StudentFlashcardSet;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.repositories.FlashcardRepository;
import org.example.technihongo.repositories.StudentFlashcardSetRepository;
import org.example.technihongo.services.interfaces.FlashcardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FlashcardServiceImpl implements FlashcardService {
    @Autowired
    private FlashcardRepository flashcardRepository;
    @Autowired
    private StudentFlashcardSetRepository studentFlashcardSetRepository;
    @Override
    public FlashcardResponseDTO createFlashcard(Integer studentId, Integer flashcardSetId, FlashcardRequestDTO request) {
        StudentFlashcardSet flashcardSet = studentFlashcardSetRepository.findById(flashcardSetId)
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard Set not found with id: " + flashcardSetId));

        if (!flashcardSet.getCreator().getStudentId().equals(studentId)) {
            throw new IllegalArgumentException("You don't have permission to add flashcards to this set");
        }

        Flashcard flashcard = new Flashcard();
        flashcard.setDefinition(request.getJapaneseDefinition());
        flashcard.setTranslation(request.getVietEngTranslation());
        flashcard.setImgUrl(request.getImageUrl());
        flashcard.setStudentFlashCardSet(flashcardSet);

        flashcard = flashcardRepository.save(flashcard);
        return convertToFlashcardResponseDTO(flashcard);
    }

    @Override
    public FlashcardResponseDTO updateFlashcard(Integer studentId, Integer flashcardId, FlashcardRequestDTO request) {
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard not found with Id: " + flashcardId));

        if (!flashcard.getStudentFlashCardSet().getCreator().getStudentId().equals(studentId)) {
            throw new RuntimeException("You don't have permission to update this flashcard");
        }
        flashcard.setDefinition(request.getJapaneseDefinition());
        flashcard.setTranslation(request.getVietEngTranslation());
        flashcard.setImgUrl(request.getImageUrl());

        flashcard = flashcardRepository.save(flashcard);
        return convertToFlashcardResponseDTO(flashcard);
    }

    @Override
    public void deleteFlashcard(Integer studentId, Integer flashcardId) {
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard not found with id: " + flashcardId));

        if (!flashcard.getStudentFlashCardSet().getCreator().getStudentId().equals(studentId)) {
            throw new RuntimeException("You don't have permission to delete this flashcard");
        }

        flashcardRepository.deleteById(flashcardId);
    }

    @Override
    public FlashcardResponseDTO getFlashcardById(Integer flashcardId) {
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard not found with id: " + flashcardId));
        return convertToFlashcardResponseDTO(flashcard);
    }
    private FlashcardResponseDTO convertToFlashcardResponseDTO(Flashcard flashcard) {
        FlashcardResponseDTO response = new FlashcardResponseDTO();
        response.setFlashcardId(flashcard.getFlashCardId());
        response.setJapaneseDefinition(flashcard.getDefinition());
        response.setVietEngTranslation(flashcard.getTranslation());
        response.setImageUrl(flashcard.getImgUrl());
        return response;
    }
}
