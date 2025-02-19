package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.dto.FlashcardResponseDTO;
import org.example.technihongo.dto.FlashcardSetRequestDTO;
import org.example.technihongo.dto.FlashcardSetResponseDTO;
import org.example.technihongo.entities.Flashcard;
import org.example.technihongo.entities.Student;
import org.example.technihongo.entities.StudentFlashcardSet;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.exception.UnauthorizedAccessException;
import org.example.technihongo.repositories.FlashcardRepository;
import org.example.technihongo.repositories.StudentFlashcardSetRepository;
import org.example.technihongo.repositories.StudentRepository;
import org.example.technihongo.services.interfaces.StudentFlashcardSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public FlashcardSetResponseDTO createFlashcardSet(Integer studentId, FlashcardSetRequestDTO request) {
        if(request.getTitle() == null) {
            throw new IllegalArgumentException("You must fill all fields required!");
        }
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with Id: " + studentId));
        StudentFlashcardSet flashcardSet = new StudentFlashcardSet();
        flashcardSet.setTitle(request.getTitle());
        flashcardSet.setDescription(request.getDescription());
        flashcardSet.setPublic(request.getIsPublic());
        flashcardSet.setCreator(student);

        flashcardSet.setTotalCard(0);
        flashcardSet.setTotalView(0);

        flashcardSet = flashcardSetRepository.save(flashcardSet);
        return convertToFlashcardSetResponseDTO(flashcardSet);
    }

    @Override
    public FlashcardSetResponseDTO updateFlashcardSet(Integer studentId, Integer flashcardSetId, FlashcardSetRequestDTO request) {
        StudentFlashcardSet flashcardSet = flashcardSetRepository.findById(flashcardSetId)
                .orElseThrow(() -> new RuntimeException("FlashcardSet not found"));

        if (request.getTitle() != null) {
            flashcardSet.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            flashcardSet.setDescription(request.getDescription());
        }
        if (request.getIsPublic() != null) {
            flashcardSet.setPublic(request.getIsPublic());
        }

        flashcardSet = flashcardSetRepository.save(flashcardSet);
        return convertToFlashcardSetResponseDTO(flashcardSet);
    }

    @Override
    public void deleteFlashcardSet(Integer studentId, Integer flashcardSetId) {
        flashcardSetRepository.deleteById(flashcardSetId);
    }

    @Override
    public FlashcardSetResponseDTO getFlashcardSetById(Integer flashcardSetId) {
        StudentFlashcardSet flashcardSet = flashcardSetRepository.findById(flashcardSetId)
                .orElseThrow(() -> new RuntimeException("FlashcardSet not found"));
        return convertToFlashcardSetResponseDTO(flashcardSet);    }



    @Override
    public FlashcardSetResponseDTO updateFlashcardSetVisibility(Integer studentId, Integer flashcardSetId, Boolean isPublic) {
        StudentFlashcardSet flashcardSet = flashcardSetRepository.findById(flashcardSetId)
                .orElseThrow(() -> new RuntimeException("FlashcardSet not found"));
        flashcardSet.setPublic(isPublic);
        flashcardSet = flashcardSetRepository.save(flashcardSet);
        return convertToFlashcardSetResponseDTO(flashcardSet);    }

    @Override
    public FlashcardSetResponseDTO getAllFlashcardsInSet(Integer studentId,Integer flashcardSetId) {
        StudentFlashcardSet flashcardSet = flashcardSetRepository.findById(flashcardSetId)
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard Set not found with id: " + flashcardSetId));

        if(!flashcardSet.getCreator().getStudentId().equals(studentId)) {
            throw new UnauthorizedAccessException("You do not have permission to access this Flashcard Set.");
        }
        List<Flashcard> flashcards = flashcardRepository.findByStudentFlashCardSetStudentSetId(flashcardSetId);

        FlashcardSetResponseDTO response = new FlashcardSetResponseDTO();
        response.setStudentSetId(flashcardSet.getStudentSetId());
        response.setTitle(flashcardSet.getTitle());
        response.setDescription(flashcardSet.getDescription());
        response.setIsPublic(flashcardSet.isPublic());
        response.setFlashcards(flashcards.stream()
                .map(this::convertToFlashcardResponseDTO)
                .collect(Collectors.toList()));

        return response;
    }

    @Override
    public List<FlashcardSetResponseDTO> studentFlashcardList(Integer studentId) {
        List<StudentFlashcardSet> flashcardSets = flashcardSetRepository.findByCreatorStudentId(studentId);
        return flashcardSets.stream()
                .map(this::convertToFlashcardSetResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlashcardSetResponseDTO> searchTitle(String keyword) {
        if(keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<StudentFlashcardSet> studentFlashcardSets = flashcardSetRepository.findByTitleContainingIgnoreCase(keyword.trim());
        return studentFlashcardSets.stream()
                .map(this::convertToFlashcardSetResponseDTO)
                .collect(Collectors.toList());
    }


    private FlashcardResponseDTO convertToFlashcardResponseDTO(Flashcard flashcard) {
        FlashcardResponseDTO response = new FlashcardResponseDTO();
        response.setFlashcardId(flashcard.getFlashCardId());
        response.setJapaneseDefinition(flashcard.getDefinition());
        response.setVietEngTranslation(flashcard.getTranslation());
        response.setImageUrl(flashcard.getImgUrl());
        return response;
    }

    private FlashcardSetResponseDTO convertToFlashcardSetResponseDTO(StudentFlashcardSet flashcardSet) {
        FlashcardSetResponseDTO response = new FlashcardSetResponseDTO();
        response.setStudentSetId(flashcardSet.getStudentSetId());
        response.setTitle(flashcardSet.getTitle());
        response.setDescription(flashcardSet.getDescription());
        response.setIsPublic(flashcardSet.isPublic());

        List<Flashcard> flashcards = flashcardRepository.findByStudentFlashCardSetStudentSetId(flashcardSet.getStudentSetId());
        response.setFlashcards(flashcards.stream()
                .map(this::convertToFlashcardResponseDTO)
                .collect(Collectors.toList()));

        return response;
    }

}
