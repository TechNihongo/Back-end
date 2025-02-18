package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.dto.FlashcardResponseDTO;
import org.example.technihongo.dto.SystemFlashcardSetRequestDTO;
import org.example.technihongo.dto.SystemFlashcardSetResponseDTO;
import org.example.technihongo.entities.*;
import org.example.technihongo.enums.DifficultyLevelEnum;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.exception.UnauthorizedAccessException;
import org.example.technihongo.repositories.*;
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
    @Autowired
    private DomainRepository domainRepository;
    @Autowired
    private DifficultyLevelRepository difficultyLevelRepository;

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
        flashcardSet.setPublic(requestDTO.getIsPublic());
        flashcardSet.setPremium(requestDTO.getIsPremium());
        flashcardSet.setCreator(user);

        if(requestDTO.getDomainId() != null) {
            Domain domain = domainRepository.findById(requestDTO.getDomainId())
                    .orElseThrow(() -> new ResourceNotFoundException("Domain not found with Id: " + requestDTO.getDomainId()));
            flashcardSet.setDomain(domain);
        }

        if (requestDTO.getDifficultyLevel() != null) {
            DifficultyLevel difficultyLevel = difficultyLevelRepository.findByTag(requestDTO.getDifficultyLevel());

            if (difficultyLevel == null) {
                throw new ResourceNotFoundException("DifficultyLevel not found: " + requestDTO.getDifficultyLevel());
            }
            flashcardSet.setDifficultyLevel(difficultyLevel);
        }

        flashcardSet = systemFlashcardSetRepository.save(flashcardSet);
        return convertToSystemFlashcardSetResponseDTO(flashcardSet);
    }

    @Override
    public SystemFlashcardSetResponseDTO update(Integer userId, Integer flashcardSetId, SystemFlashcardSetRequestDTO requestDTO) {
        SystemFlashcardSet flashcardSet = systemFlashcardSetRepository.findById(flashcardSetId)
                .orElseThrow(() -> new ResourceNotFoundException("FlashcardSet not found with Id: " + flashcardSetId));

        if (!flashcardSet.getCreator().getUserId().equals(userId)) {
            throw new RuntimeException("You do not have permission to update this flashcard set.");
        }

        if (requestDTO.getTitle() != null) {
            flashcardSet.setTitle(requestDTO.getTitle());
        }
        if (requestDTO.getDescription() != null) {
            flashcardSet.setDescription(requestDTO.getDescription());
        }
        if (requestDTO.getIsPremium() != null) {
            flashcardSet.setPremium(requestDTO.getIsPremium());
        }

        if (requestDTO.getDomainId() != null) {
            Domain domain = domainRepository.findById(requestDTO.getDomainId())
                    .orElseThrow(() -> new ResourceNotFoundException("Domain not found with Id: " + requestDTO.getDomainId()));
            flashcardSet.setDomain(domain);
        }

        if (requestDTO.getDifficultyLevel() != null) {
            DifficultyLevel difficultyLevel = difficultyLevelRepository.findByTag(DifficultyLevelEnum.valueOf(requestDTO.getDifficultyLevel().name()));
            if (difficultyLevel == null) {
                throw new ResourceNotFoundException("DifficultyLevel not found: " + requestDTO.getDifficultyLevel());
            }
            flashcardSet.setDifficultyLevel(difficultyLevel);
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
    public SystemFlashcardSetResponseDTO getAllFlashcardsInSet(Integer userId ,Integer flashcardSetId) {
        SystemFlashcardSet flashcardSet = systemFlashcardSetRepository.findById(flashcardSetId)
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard Set not found with id: " + flashcardSetId));

        if(!flashcardSet.getCreator().getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not have permission to access this Flashcard Set.");
        }

        List<Flashcard> flashcards = flashcardRepository.findByStudentFlashCardSetStudentSetId(flashcardSetId);

        List<FlashcardResponseDTO> flashcardDTOs = flashcards.stream()
                .map(this::convertToFlashcardResponseDTO)
                .collect(Collectors.toList());

        SystemFlashcardSetResponseDTO responseDTO = new SystemFlashcardSetResponseDTO();
        responseDTO.setContentManagerId(flashcardSet.getCreator().getUserId());
        responseDTO.setTitle(flashcardSet.getTitle());
        responseDTO.setDescription(flashcardSet.getDescription());
        responseDTO.setIsPublic(flashcardSet.isPublic());
        responseDTO.setIsPremium(flashcardSet.isPremium());
        responseDTO.setDomainId(flashcardSet.getDomain().getDomainId());
        responseDTO.setDifficultyLevel(flashcardSet.getDifficultyLevel().getTag());
        responseDTO.setFlashcards(flashcardDTOs);

        return responseDTO;
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
        response.setIsPremium(flashcardSet.isPremium());
        response.setDomainId(flashcardSet.getDomain() != null ? flashcardSet.getDomain().getDomainId() : null);
        response.setDifficultyLevel(flashcardSet.getDifficultyLevel() != null ? flashcardSet.getDifficultyLevel().getTag() : null);

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
