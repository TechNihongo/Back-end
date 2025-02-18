package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.dto.FlashcardResponseDTO;
import org.example.technihongo.dto.SystemFlashcardSetRequestDTO;
import org.example.technihongo.dto.SystemFlashcardSetResponseDTO;
import org.example.technihongo.entities.Flashcard;
import org.example.technihongo.entities.SystemFlashcardSet;
import org.example.technihongo.entities.User;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.repositories.FlashcardRepository;
import org.example.technihongo.repositories.SystemFlashcardSetRepository;
import org.example.technihongo.repositories.UserRepository;
import org.example.technihongo.services.interfaces.SystemFlashcardSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SystemFlashcardSetServiceImpl implements SystemFlashcardSetService {
    @Autowired
    private SystemFlashcardSetRepository systemFlashcardSetRepository;
    @Autowired
    private FlashcardRepository flashcardRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public SystemFlashcardSetResponseDTO create(Integer userId, SystemFlashcardSetRequestDTO requestDTO) {
        if (requestDTO.getTitle() == null) {
            throw new IllegalArgumentException("You must fill all fields required!");
        }

        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new ResourceNotFoundException("Content Manager not found with Id: " + userId);
        }

        SystemFlashcardSet flashcardSet = new SystemFlashcardSet();
        flashcardSet.setTitle(requestDTO.getTitle());
        flashcardSet.setDescription(requestDTO.getDescription());
        flashcardSet.setPublic(requestDTO.isPublic());
        flashcardSet.setCreator(user);

        flashcardSet = systemFlashcardSetRepository.save(flashcardSet);
        return convertToSystemFlashcardSetResponseDTO(flashcardSet);
    }

    @Override
    public SystemFlashcardSetResponseDTO update(Integer userId, Integer flashcardSetId, SystemFlashcardSetRequestDTO requestDTO) {
        SystemFlashcardSet flashcardSet = systemFlashcardSetRepository.findById(flashcardSetId)
                .orElseThrow(() -> new ResourceNotFoundException("FlashcardSet not found with Id: " + flashcardSetId));

        if (requestDTO.getTitle() != null) {
            flashcardSet.setTitle(requestDTO.getTitle());
        }
        if (requestDTO.getDescription() != null) {
            flashcardSet.setDescription(requestDTO.getDescription());
        }
        flashcardSet = systemFlashcardSetRepository.save(flashcardSet);
        return convertToSystemFlashcardSetResponseDTO(flashcardSet);
    }

    @Override
    public void deleteSystemFlashcardSet(Integer userId, Integer flashcardSetId) {
        SystemFlashcardSet flashcardSet = systemFlashcardSetRepository.findById(flashcardSetId)
                .orElseThrow(() -> new ResourceNotFoundException("FlashcardSet not found with Id: " + flashcardSetId));

        systemFlashcardSetRepository.delete(flashcardSet);
    }

    @Override
    public SystemFlashcardSetResponseDTO getSystemFlashcardSetById(Integer flashcardSetId) {
        SystemFlashcardSet flashcardSet = systemFlashcardSetRepository.findById(flashcardSetId)
                .orElseThrow(() -> new ResourceNotFoundException("FlashcardSet not found with Id: " + flashcardSetId));

        return convertToSystemFlashcardSetResponseDTO(flashcardSet);
    }

    @Override
    public SystemFlashcardSetResponseDTO updateSystemFlashcardSetVisibility(Integer userId, Integer flashcardSetId, Boolean isPublic) {
        SystemFlashcardSet flashcardSet = systemFlashcardSetRepository.findById(flashcardSetId)
                .orElseThrow(() -> new ResourceNotFoundException("FlashcardSet not found with Id: " + flashcardSetId));

        flashcardSet.setPublic(isPublic);
        flashcardSet = systemFlashcardSetRepository.save(flashcardSet);
        return convertToSystemFlashcardSetResponseDTO(flashcardSet);
    }

    @Override
    public SystemFlashcardSetResponseDTO getAllFlashcardsInSet(Integer flashcardSetId) {
        return null;
    }

    @Override
    public List<SystemFlashcardSetResponseDTO> systemFlashcardList(Integer userId) {
        List<SystemFlashcardSet> flashcardSets = systemFlashcardSetRepository.findByCreatorUserId(userId);
        return flashcardSets.stream()
                .map(this::convertToSystemFlashcardSetResponseDTO)
                .collect(Collectors.toList());
    }

    private SystemFlashcardSetResponseDTO convertToSystemFlashcardSetResponseDTO(SystemFlashcardSet flashcardSet) {
        SystemFlashcardSetResponseDTO response = new SystemFlashcardSetResponseDTO();
        response.setContentManagerId(flashcardSet.getCreator().getUserId());
        response.setTitle(flashcardSet.getTitle());
        response.setDescription(flashcardSet.getDescription());
        response.setIsPublic(flashcardSet.isPublic());

        List<Flashcard> flashcards = flashcardRepository.findBySystemFlashCardSetSystemSetId(flashcardSet.getSystemSetId());
        response.setFlashcards(flashcards.stream()
                .map(this::convertToFlashcardResponseDTO)
                .collect(Collectors.toList()));

        return response;
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
