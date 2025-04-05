package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.dto.*;
import org.example.technihongo.entities.Flashcard;
import org.example.technihongo.entities.LearningResource;
import org.example.technihongo.entities.Student;
import org.example.technihongo.entities.StudentFlashcardSet;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.exception.UnauthorizedAccessException;
import org.example.technihongo.repositories.FlashcardRepository;
import org.example.technihongo.repositories.LearningResourceRepository;
import org.example.technihongo.repositories.StudentFlashcardSetRepository;
import org.example.technihongo.repositories.StudentRepository;
import org.example.technihongo.services.interfaces.StudentFlashcardSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentFlashcardSetServiceImpl implements StudentFlashcardSetService {
    @Autowired
    private StudentFlashcardSetRepository flashcardSetRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FlashcardRepository flashcardRepository;
    @Autowired
    private LearningResourceRepository learningResourceRepository;

    @Override
    public FlashcardSetResponseDTO createFlashcardSet(Integer studentId, FlashcardSetRequestDTO request) {
        if (request.getTitle() == null) {
            throw new IllegalArgumentException("Title is required!");
        }
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with Id: " + studentId));
        StudentFlashcardSet flashcardSet = new StudentFlashcardSet();
        flashcardSet.setTitle(request.getTitle());
        flashcardSet.setDescription(request.getDescription());
        flashcardSet.setPublic(request.getIsPublic());
        flashcardSet.setCreator(student);
        flashcardSet.setDeleted(false);
        flashcardSet.setViolated(false);
        flashcardSet.setTotalCards(0);
        flashcardSet.setTotalViews(0);

        flashcardSet = flashcardSetRepository.save(flashcardSet);
        return convertToFlashcardSetResponseDTO(flashcardSet);
    }

    @Override
    public FlashcardSetResponseDTO updateFlashcardSet(Integer studentId, Integer flashcardSetId, FlashcardSetRequestDTO request) {
        StudentFlashcardSet flashcardSet = flashcardSetRepository.findById(flashcardSetId)
                .orElseThrow(() -> new RuntimeException("FlashcardSet not found"));

        if (!flashcardSet.getCreator().getStudentId().equals(studentId)) {
            throw new UnauthorizedAccessException("You do not have permission to update this flashcard set.");
        }

        if (request.getTitle() != null) {
            flashcardSet.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            flashcardSet.setDescription(request.getDescription());
        }
        if (request.getIsPublic() != null) {
            flashcardSet.setPublic(request.getIsPublic());
        }

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE); // Lấy tất cả để đếm
        Page<Flashcard> flashcardPage = flashcardRepository.findByStudentFlashCardSetStudentSetId(flashcardSetId, pageable);
        flashcardSet.setTotalCards((int) flashcardPage.getTotalElements());

        flashcardSet = flashcardSetRepository.save(flashcardSet);
        return convertToFlashcardSetResponseDTO(flashcardSet);
    }

    @Override
    public void deleteFlashcardSet(Integer studentId, Integer flashcardSetId) {
        StudentFlashcardSet flashcardSet = getActiveFlashcardSet(flashcardSetId);

        if (!flashcardSet.getCreator().getStudentId().equals(studentId)) {
            throw new UnauthorizedAccessException("You do not have permission to delete this flashcard set.");
        }
        flashcardSetRepository.deleteById(flashcardSetId);
    }

    @Override
    public List<FlashcardSetResponseDTO> getFlashcardSetsByStudentId(Integer studentId) {
        List<StudentFlashcardSet> flashcardSets = flashcardSetRepository.findByCreatorAndPublicStatus(studentId, true);
        return flashcardSets.stream()
                .map(this::convertToFlashcardSetResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlashcardSetResponseDTO> getFlashcardSetsByPublicStatus() {
        List<StudentFlashcardSet> flashcardSets = flashcardSetRepository.findAllPublicSetsOrderByViewsDesc();
        return flashcardSets.stream()
                .map(this::convertToFlashcardSetResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateFlashcardOrder(Integer studentId, Integer flashcardSetId, UpdateFlashcardOrderDTO updateFlashcardOrderDTO) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }
        if (flashcardSetId == null) {
            throw new IllegalArgumentException("Flashcard set ID cannot be null");
        }
        if (updateFlashcardOrderDTO == null ||
                updateFlashcardOrderDTO.getNewFlashcardOrder() == null ||
                updateFlashcardOrderDTO.getNewFlashcardOrder().isEmpty()) {
            throw new IllegalArgumentException("New flashcard order cannot be null or empty");
        }

        StudentFlashcardSet flashcardSet = flashcardSetRepository.findById(flashcardSetId)
                .orElseThrow(() -> new ResourceNotFoundException("Student Flashcard Set not found with ID: " + flashcardSetId));

        if (flashcardSet.isDeleted()) {
            throw new ResourceNotFoundException("Student Flashcard Set has been deleted");
        }
        if (!flashcardSet.getCreator().getStudentId().equals(studentId)) {
            throw new UnauthorizedAccessException("You do not have permission to update this student flashcard set");
        }

        List<Flashcard> flashcards = flashcardRepository.findAllById(updateFlashcardOrderDTO.getNewFlashcardOrder());

        if (flashcards.size() != updateFlashcardOrderDTO.getNewFlashcardOrder().size()) {
            throw new ResourceNotFoundException("One or more flashcards not found");
        }

        for (Flashcard flashcard : flashcards) {
            if (flashcard.getStudentFlashCardSet() == null ||
                    !flashcard.getStudentFlashCardSet().getStudentSetId().equals(flashcardSetId)) {
                throw new IllegalArgumentException("Flashcard " + flashcard.getFlashCardId() +
                        " does not belong to the specified student flashcard set");
            }
        }

        Map<Integer, Flashcard> flashcardMap = flashcards.stream()
                .collect(Collectors.toMap(Flashcard::getFlashCardId, Function.identity()));

        List<Integer> newOrder = updateFlashcardOrderDTO.getNewFlashcardOrder();
        for (int i = 0; i < newOrder.size(); i++) {
            Integer flashcardId = newOrder.get(i);
            Flashcard flashcard = flashcardMap.get(flashcardId);
            if (flashcard != null) {
                flashcard.setCardOrder(i + 1);
            }
        }

        flashcardRepository.saveAll(flashcards);
    }

    @Override
    public FlashcardSetResponseDTO updateFlashcardSetVisibility(Integer studentId, Integer flashcardSetId, Boolean isPublic) {
        StudentFlashcardSet flashcardSet = getActiveFlashcardSet(flashcardSetId);

        if (!flashcardSet.getCreator().getStudentId().equals(studentId)) {
            throw new UnauthorizedAccessException("You do not have permission to update visibility of this flashcard set.");
        }
        flashcardSet.setPublic(isPublic);
        flashcardSet = flashcardSetRepository.save(flashcardSet);
        return convertToFlashcardSetResponseDTO(flashcardSet);
    }

    @Override
    public FlashcardSetResponseDTO getAllFlashcardsInSet(Integer studentId, Integer flashcardSetId) {
        StudentFlashcardSet flashcardSet = getActiveFlashcardSet(flashcardSetId);

        if (!flashcardSet.getCreator().getStudentId().equals(studentId)) {
            flashcardSet.setTotalViews(flashcardSet.getTotalViews() + 1);
            flashcardSetRepository.save(flashcardSet);
        }

        if (!flashcardSet.isPublic() && !flashcardSet.getCreator().getStudentId().equals(studentId)) {
            throw new UnauthorizedAccessException("You do not have permission to access this flashcard set.");
        }

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("cardOrder").ascending());
        Page<Flashcard> flashcardPage = flashcardRepository.findByStudentFlashCardSetStudentSetId(flashcardSetId, pageable);
        flashcardSet.setTotalCards((int) flashcardPage.getTotalElements());

        FlashcardSetResponseDTO response = new FlashcardSetResponseDTO();
        response.setStudentId(flashcardSet.getCreator().getStudentId());
        response.setStudentSetId(flashcardSet.getStudentSetId());
        response.setTitle(flashcardSet.getTitle());
        response.setDescription(flashcardSet.getDescription());
        response.setIsPublic(flashcardSet.isPublic());
        response.setFlashcards(flashcardPage.getContent().stream()
                .map(this::convertToFlashcardResponseDTO)
                .collect(Collectors.toList()));

        return response;
    }

    @Override
    public List<FlashcardSetResponseDTO> studentFlashcardList(Integer studentId) {
        List<StudentFlashcardSet> flashcardSets = flashcardSetRepository.findByCreatorStudentId(studentId);
        return flashcardSets.stream()
                .filter(set -> !set.isDeleted())
                .map(this::convertToFlashcardSetResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlashcardSetResponseDTO> searchTitle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<StudentFlashcardSet> studentFlashcardSets = flashcardSetRepository.findByTitleContainingIgnoreCase(keyword.trim());
        return studentFlashcardSets.stream()
                .filter(set -> !set.isDeleted())
                .map(this::convertToFlashcardSetResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FlashcardSetResponseDTO createFlashcardSetFromResource(Integer studentId, CreateFlashcardSetFromResourceDTO request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with Id: " + studentId));

        LearningResource resource = learningResourceRepository.findByResourceId(request.getResourceId());
        if (resource == null) {
            throw new ResourceNotFoundException("Resource not found with Id: " + request.getResourceId());
        }
        if (!StringUtils.hasText(resource.getVideoUrl())) {
            throw new IllegalArgumentException("Resource must have a video URL to create Flashcard Set.");
        }
        if (request.getFlashcards() == null || request.getFlashcards().isEmpty()) {
            throw new IllegalArgumentException("You must provide at least one Flashcard to create Flashcard Set.");
        }

        StudentFlashcardSet flashcardSet = StudentFlashcardSet.builder()
                .creator(student)
                .learningResource(resource)
                .title(StringUtils.hasText(request.getTitle()) ? request.getTitle() : resource.getTitle())
                .description(request.getDescription())
                .isPublic(request.getIsPublic() != null ? request.getIsPublic() : true)
                .totalCards(0)
                .totalViews(0)
                .isViolated(false)
                .isDeleted(false)
                .flashcards(new HashSet<>())
                .build();

        StudentFlashcardSet savedFlashcardSet = flashcardSetRepository.save(flashcardSet);

        List<Flashcard> flashcards = createFlashcards(savedFlashcardSet, request.getFlashcards());
        savedFlashcardSet.setFlashcards(new HashSet<>(flashcards));
        savedFlashcardSet.setTotalCards(flashcards.size());

        flashcardSetRepository.save(savedFlashcardSet);

        return mapToFlashcardSetResponseDTO(savedFlashcardSet, flashcards);
    }

    private FlashcardResponseDTO convertToFlashcardResponseDTO(Flashcard flashcard) {
        FlashcardResponseDTO response = new FlashcardResponseDTO();
        response.setFlashcardId(flashcard.getFlashCardId());
        response.setJapaneseDefinition(flashcard.getDefinition());
        response.setVietEngTranslation(flashcard.getTranslation());
        response.setCardOrder(flashcard.getCardOrder());
        response.setImageUrl(flashcard.getImgUrl());
        return response;
    }

    private FlashcardSetResponseDTO convertToFlashcardSetResponseDTO(StudentFlashcardSet flashcardSet) {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("cardOrder").ascending());
        Page<Flashcard> flashcardPage = flashcardRepository.findByStudentFlashCardSetStudentSetId(flashcardSet.getStudentSetId(), pageable);

        FlashcardSetResponseDTO response = new FlashcardSetResponseDTO();
        response.setStudentId(flashcardSet.getCreator().getStudentId());
        response.setStudentSetId(flashcardSet.getStudentSetId());
        response.setTitle(flashcardSet.getTitle());
        response.setDescription(flashcardSet.getDescription());
        response.setIsPublic(flashcardSet.isPublic());
        response.setFlashcards(flashcardPage.getContent().stream()
                .map(this::convertToFlashcardResponseDTO)
                .collect(Collectors.toList()));

        return response;
    }

    private List<Flashcard> createFlashcards(StudentFlashcardSet flashcardSet, List<FlashcardRequestDTO> flashcardDTOs) {
        List<Flashcard> flashcards = new ArrayList<>();

        for (int i = 0; i < flashcardDTOs.size(); i++) {
            FlashcardRequestDTO dto = flashcardDTOs.get(i);

            Flashcard flashcard = Flashcard.builder()
                    .studentFlashCardSet(flashcardSet)
                    .definition(dto.getJapaneseDefinition())
                    .translation(dto.getVietEngTranslation())
                    .imgUrl(dto.getImageUrl())
                    .cardOrder(i + 1)
                    .build();

            flashcards.add(flashcardRepository.save(flashcard));
        }

        return flashcards;
    }

    private FlashcardSetResponseDTO mapToFlashcardSetResponseDTO(StudentFlashcardSet flashcardSet, List<Flashcard> flashcards) {
        List<FlashcardResponseDTO> flashcardDTOs = flashcards.stream()
                .map(this::mapToFlashcardResponseDTO)
                .collect(Collectors.toList());

        FlashcardSetResponseDTO responseDTO = new FlashcardSetResponseDTO();
        responseDTO.setStudentId(flashcardSet.getCreator().getStudentId());
        responseDTO.setStudentSetId(flashcardSet.getStudentSetId());
        responseDTO.setTitle(flashcardSet.getTitle());
        responseDTO.setDescription(flashcardSet.getDescription());
        responseDTO.setIsPublic(flashcardSet.isPublic());
        responseDTO.setFlashcards(flashcardDTOs);

        return responseDTO;
    }

    private FlashcardResponseDTO mapToFlashcardResponseDTO(Flashcard flashcard) {
        FlashcardResponseDTO responseDTO = new FlashcardResponseDTO();
        responseDTO.setFlashcardId(flashcard.getFlashCardId());
        responseDTO.setJapaneseDefinition(flashcard.getDefinition());
        responseDTO.setVietEngTranslation(flashcard.getTranslation());
        responseDTO.setCardOrder(flashcard.getCardOrder());
        responseDTO.setImageUrl(flashcard.getImgUrl());

        return responseDTO;
    }

    private StudentFlashcardSet getActiveFlashcardSet(Integer flashcardSetId) {
        StudentFlashcardSet flashcardSet = flashcardSetRepository.findById(flashcardSetId)
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard Set not found with id: " + flashcardSetId));
        if (flashcardSet.isDeleted()) {
            throw new ResourceNotFoundException("Flashcard Set has been deleted and cannot be accessed.");
        }
        return flashcardSet;
    }
}