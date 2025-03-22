package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.dto.FlashcardResponseDTO;
import org.example.technihongo.dto.SystemFlashcardSetRequestDTO;
import org.example.technihongo.dto.SystemFlashcardSetResponseDTO;
import org.example.technihongo.entities.DifficultyLevel;
import org.example.technihongo.entities.Flashcard;
import org.example.technihongo.entities.SystemFlashcardSet;
import org.example.technihongo.entities.User;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.exception.UnauthorizedAccessException;
import org.example.technihongo.repositories.DifficultyLevelRepository;
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
    @Autowired
    private DifficultyLevelRepository difficultyLevelRepository;

    @Override
    public SystemFlashcardSetResponseDTO create(Integer userId, SystemFlashcardSetRequestDTO requestDTO) {
        if (requestDTO.getTitle() == null) {
            throw new IllegalArgumentException("Title is required!");
        }

        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new ResourceNotFoundException("Content Manager not found with Id: " + userId);
        }

        SystemFlashcardSet flashcardSet = new SystemFlashcardSet();
        flashcardSet.setTitle(requestDTO.getTitle());
        flashcardSet.setDescription(requestDTO.getDescription());
        flashcardSet.setPublic(requestDTO.getIsPublic() != null ? requestDTO.getIsPublic() : false);
        flashcardSet.setPremium(requestDTO.getIsPremium() != null ? requestDTO.getIsPremium() : false);
        flashcardSet.setTotalCards(0); // Khởi tạo là 0, sẽ cập nhật khi thêm flashcard
        flashcardSet.setCreator(user);
        flashcardSet.setDeleted(false);

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
            throw new UnauthorizedAccessException("You do not have permission to update this flashcard set.");
        }

        if (requestDTO.getTitle() != null) {
            flashcardSet.setTitle(requestDTO.getTitle());
        }
        if (requestDTO.getDescription() != null) {
            flashcardSet.setDescription(requestDTO.getDescription());
        }
        if (requestDTO.getIsPublic() != null) {
            flashcardSet.setPublic(requestDTO.getIsPublic());
        }
        if (requestDTO.getIsPremium() != null) {
            flashcardSet.setPremium(requestDTO.getIsPremium());
        }

        if (requestDTO.getDifficultyLevel() != null) {
            DifficultyLevel difficultyLevel = difficultyLevelRepository.findByTag(requestDTO.getDifficultyLevel());
            if (difficultyLevel == null) {
                throw new ResourceNotFoundException("DifficultyLevel not found: " + requestDTO.getDifficultyLevel());
            }
            flashcardSet.setDifficultyLevel(difficultyLevel);
        }
        int totalCards = flashcardRepository.findBySystemFlashCardSetSystemSetId(flashcardSetId).size();
        flashcardSet.setTotalCards(totalCards);

        flashcardSet = systemFlashcardSetRepository.save(flashcardSet);
        return convertToSystemFlashcardSetResponseDTO(flashcardSet);
    }

    @Override
    public void deleteSystemFlashcardSet(Integer userId, Integer flashcardSetId) {
        SystemFlashcardSet flashcardSet = systemFlashcardSetRepository.findById(flashcardSetId)
                .orElseThrow(() -> new ResourceNotFoundException("FlashcardSet not found with Id: " + flashcardSetId));

        if (!flashcardSet.getCreator().getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not have permission to delete this flashcard set.");
        }

        flashcardSet.setDeleted(true);
        systemFlashcardSetRepository.save(flashcardSet);
    }

    @Override
    public SystemFlashcardSetResponseDTO getSystemFlashcardSetById(Integer flashcardSetId) {
        SystemFlashcardSet flashcardSet = getActiveFlashcardSet(flashcardSetId);
        return convertToSystemFlashcardSetResponseDTO(flashcardSet);
    }

    @Override
    public SystemFlashcardSetResponseDTO updateSystemFlashcardSetVisibility(Integer userId, Integer flashcardSetId, Boolean isPublic) {
        SystemFlashcardSet flashcardSet = getActiveFlashcardSet(flashcardSetId);

        if (!flashcardSet.getCreator().getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not have permission to update visibility of this flashcard set.");
        }

        flashcardSet.setPublic(isPublic);
        flashcardSet.setTotalCards(flashcardRepository.findBySystemFlashCardSetSystemSetId(flashcardSetId).size());
        flashcardSet = systemFlashcardSetRepository.save(flashcardSet);
        return convertToSystemFlashcardSetResponseDTO(flashcardSet);
    }

    @Override
    public SystemFlashcardSetResponseDTO getAllFlashcardsInSet(Integer userId, Integer flashcardSetId) {
        SystemFlashcardSet flashcardSet = getActiveFlashcardSet(flashcardSetId);

        if (!flashcardSet.getCreator().getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not have permission to access this Flashcard Set.");
        }

        List<Flashcard> flashcards = flashcardRepository.findBySystemFlashCardSetSystemSetId(flashcardSetId);
        flashcardSet.setTotalCards(flashcards.size());
        systemFlashcardSetRepository.save(flashcardSet);

        SystemFlashcardSetResponseDTO responseDTO = new SystemFlashcardSetResponseDTO();
        responseDTO.setContentManagerId(flashcardSet.getCreator().getUserId());
        responseDTO.setTitle(flashcardSet.getTitle());
        responseDTO.setDescription(flashcardSet.getDescription());
        responseDTO.setIsPublic(flashcardSet.isPublic());
        responseDTO.setIsPremium(flashcardSet.isPremium());
        responseDTO.setDifficultyLevel(flashcardSet.getDifficultyLevel() != null ? flashcardSet.getDifficultyLevel().getTag() : null);
        responseDTO.setFlashcards(flashcards.stream()
                .map(this::convertToFlashcardResponseDTO)
                .collect(Collectors.toList()));

        return responseDTO;
    }

    @Override
    public List<SystemFlashcardSetResponseDTO> systemFlashcardList(Integer userId) {
        List<SystemFlashcardSet> flashcardSets = systemFlashcardSetRepository.findByCreatorUserId(userId);
        return flashcardSets.stream()
                .filter(set -> !set.isDeleted()) // Chỉ lấy các set chưa bị xóa
                .map(this::convertToSystemFlashcardSetResponseDTO)
                .collect(Collectors.toList());
    }

    private SystemFlashcardSet getActiveFlashcardSet(Integer flashcardSetId) {
        SystemFlashcardSet flashcardSet = systemFlashcardSetRepository.findById(flashcardSetId)
                .orElseThrow(() -> new ResourceNotFoundException("FlashcardSet not found with Id: " + flashcardSetId));
        if (flashcardSet.isDeleted()) {
            throw new ResourceNotFoundException("FlashcardSet has been deleted and cannot be accessed.");
        }
        flashcardSet.setTotalCards(flashcardRepository.findBySystemFlashCardSetSystemSetId(flashcardSetId).size());
        systemFlashcardSetRepository.save(flashcardSet);
        return flashcardSet;
    }

    private SystemFlashcardSetResponseDTO convertToSystemFlashcardSetResponseDTO(SystemFlashcardSet flashcardSet) {
        SystemFlashcardSetResponseDTO response = new SystemFlashcardSetResponseDTO();
        response.setContentManagerId(flashcardSet.getCreator().getUserId());
        response.setTitle(flashcardSet.getTitle());
        response.setDescription(flashcardSet.getDescription());
        response.setIsPublic(flashcardSet.isPublic());
        response.setIsPremium(flashcardSet.isPremium());
        response.setDifficultyLevel(flashcardSet.getDifficultyLevel() != null ? flashcardSet.getDifficultyLevel().getTag() : null);

        List<Flashcard> flashcards = flashcardRepository.findBySystemFlashCardSetSystemSetId(flashcardSet.getSystemSetId());
        flashcardSet.setTotalCards(flashcards.size());
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