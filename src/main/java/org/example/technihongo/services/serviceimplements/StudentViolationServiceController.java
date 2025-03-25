package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.StudentViolationDTO;
import org.example.technihongo.entities.StudentViolation;
import org.example.technihongo.enums.ViolationStatus;
import org.example.technihongo.repositories.StudentViolationRepository;
import org.example.technihongo.services.interfaces.StudentViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentViolationServiceController implements StudentViolationService {
    @Autowired
    private final StudentViolationRepository studentViolationRepository;


    @Override
    public List<StudentViolationDTO> getAllStudentViolations(String classifyBy, String status, int pageNo, int pageSize, String sortBy, String sortDir) {
        if (!"StudentFlashcardSet".equalsIgnoreCase(classifyBy) && !"StudentCourseRating".equalsIgnoreCase(classifyBy)) {
            throw new IllegalArgumentException("classifyBy must be 'StudentFlashcardSet' or 'StudentCourseRating'.");
        }

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<StudentViolation> violations = studentViolationRepository.findByClassifyByAndStatus(
                classifyBy.toLowerCase(),
                status != null ? status.toUpperCase() : null,
                pageable);

        return violations.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private StudentViolationDTO mapToDTO(StudentViolation sv) {
        return StudentViolationDTO.builder()
                .violationId(sv.getViolationId())
                .studentSetId(sv.getStudentFlashcardSet() != null ? sv.getStudentFlashcardSet().getStudentSetId() : null)
                .ratingId(sv.getStudentCourseRating() != null ? sv.getStudentCourseRating().getRatingId() : null)
                .description(sv.getDescription())
                .actionTaken(sv.getActionTaken())
                .reportedById(sv.getReportedBy().getUserId())
                .handledById(sv.getHandledBy() != null ? sv.getHandledBy().getUserId() : null)
                .status(sv.getStatus())
                .createdAt(sv.getCreatedAt())
                .resolvedAt(sv.getResolvedAt())
                .build();
    }
}
