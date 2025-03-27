package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.HandleViolationRequestDTO;
import org.example.technihongo.dto.PageResponseDTO;
import org.example.technihongo.dto.ReportViolationRequestDTO;
import org.example.technihongo.entities.StudentCourseRating;
import org.example.technihongo.entities.StudentFlashcardSet;
import org.example.technihongo.entities.StudentViolation;
import org.example.technihongo.entities.User;
import org.example.technihongo.enums.ViolationStatus;
import org.example.technihongo.repositories.StudentCourseRatingRepository;
import org.example.technihongo.repositories.StudentFlashcardSetRepository;
import org.example.technihongo.repositories.StudentViolationRepository;
import org.example.technihongo.repositories.UserRepository;
import org.example.technihongo.services.interfaces.StudentViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class StudentViolationServiceImpl implements StudentViolationService {
    @Autowired
    private StudentViolationRepository studentViolationRepository;
    @Autowired
    private StudentFlashcardSetRepository studentFlashcardSetRepository;
    @Autowired
    private StudentCourseRatingRepository studentCourseRatingRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public PageResponseDTO<StudentViolation> getAllStudentViolations(String classifyBy, String status, int pageNo, int pageSize, String sortBy, String sortDir) {
        if (!"FlashcardSet".equalsIgnoreCase(classifyBy) && !"Rating".equalsIgnoreCase(classifyBy)) {
            throw new RuntimeException("classifyBy must be 'FlashcardSet' or 'Rating'.");
        }

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        ViolationStatus violationStatus = null;
        if (status != null && !status.trim().isEmpty()) {
            try {
                violationStatus = ViolationStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid status value. Must be PENDING, RESOLVED, or DISMISSED.");
            }
        }

        Page<StudentViolation> violations = studentViolationRepository.findByClassifyByAndStatus(
                classifyBy.toLowerCase(),
                violationStatus,
                pageable);

        return getPageResponseDTO(violations);
    }

    @Override
    public StudentViolation reportViolation(Integer reportedBy, ReportViolationRequestDTO request) {
        String classifyBy = request.getClassifyBy();
        if (!"FlashcardSet".equalsIgnoreCase(classifyBy) && !"Rating".equalsIgnoreCase(classifyBy)) {
            throw new RuntimeException("classifyBy must be 'FlashcardSet' or 'Rating'!");
        }

        Integer contentId = request.getContentId();
        if (contentId == null) {
            throw new RuntimeException("ContentId must not be null!");
        }

        User user = userRepository.findById(reportedBy)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + reportedBy));

        StudentViolation violation = StudentViolation.builder()
                .description(request.getDescription())
                .reportedBy(user)
                .status(ViolationStatus.PENDING)
                .build();

        if ("FlashcardSet".equalsIgnoreCase(classifyBy)) {
            StudentFlashcardSet flashcardSet = studentFlashcardSetRepository.findById(contentId)
                    .orElseThrow(() -> new RuntimeException("StudentFlashcardSet not found with ID: " + contentId));
            violation.setStudentFlashcardSet(flashcardSet);
            violation.setStudentCourseRating(null);
        } else {
            StudentCourseRating rating = studentCourseRatingRepository.findById(contentId)
                    .orElseThrow(() -> new RuntimeException("StudentCourseRating not found with ID: " + contentId));
            violation.setStudentCourseRating(rating);
            violation.setStudentFlashcardSet(null);
        }

        return studentViolationRepository.save(violation);
    }

    @Override
    public StudentViolation handleViolation(Integer violationId, Integer handledBy, HandleViolationRequestDTO request) {
        StudentViolation violation = studentViolationRepository.findByViolationId(violationId);
        if(violation == null){
            throw new RuntimeException("StudentViolation not found with ID: " + violationId);
        }

        if (!violation.getStatus().equals(ViolationStatus.PENDING)) {
            throw new RuntimeException("Violation has already been handled.");
        }

        String statusStr = request.getStatus();
        if (!"RESOLVED".equalsIgnoreCase(statusStr) && !"DISMISSED".equalsIgnoreCase(statusStr)) {
            throw new RuntimeException("Status must be 'RESOLVED' or 'DISMISSED'.");
        }
        ViolationStatus status = ViolationStatus.valueOf(statusStr.toUpperCase());

        if (handledBy == null) {
            throw new IllegalArgumentException("HandledBy must not be null.");
        }
        User admin = userRepository.findById(handledBy)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + handledBy));

        violation.setHandledBy(admin);
        violation.setActionTaken(request.getActionTaken());
        violation.setStatus(status);
        violation.setResolvedAt(LocalDateTime.now());

        return studentViolationRepository.save(violation);
    }

    private PageResponseDTO<StudentViolation> getPageResponseDTO(Page<StudentViolation> page) {
        return PageResponseDTO.<StudentViolation>builder()
                .content(page.getContent())
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
