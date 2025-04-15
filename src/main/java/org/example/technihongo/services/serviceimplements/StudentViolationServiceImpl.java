package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.*;
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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
    @Transactional
    public StudentViolation handleViolation(Integer violationId, Integer handledBy, HandleViolationRequestDTO request) {
        // Tìm vi phạm chính
        StudentViolation violation = studentViolationRepository.findByViolationId(violationId);
        if (violation == null) {
            throw new RuntimeException("Không tìm thấy StudentViolation với ID: " + violationId);
        }

        // Kiểm tra xem vi phạm có còn ở trạng thái PENDING không
        if (!violation.getStatus().equals(ViolationStatus.PENDING)) {
            throw new RuntimeException("Vi phạm đã được xử lý trước đó.");
        }

        // Xác thực trạng thái
        String statusStr = request.getStatus();
        if (!"RESOLVED".equalsIgnoreCase(statusStr) && !"DISMISSED".equalsIgnoreCase(statusStr)) {
            throw new RuntimeException("Trạng thái phải là 'RESOLVED' hoặc 'DISMISSED'.");
        }
        ViolationStatus status = ViolationStatus.valueOf(statusStr.toUpperCase());

        // Xác thực handledBy
        if (handledBy == null) {
            throw new IllegalArgumentException("handledBy không được để trống.");
        }
        User admin = userRepository.findById(handledBy)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + handledBy));

        // Cập nhật vi phạm chính
        violation.setHandledBy(admin);
        violation.setActionTaken(request.getActionTaken());
        violation.setStatus(status);
        violation.setResolvedAt(LocalDateTime.now());

        // Tìm các vi phạm PENDING liên quan
        List<StudentViolation> relatedViolations = findRelatedPendingViolations(violation);

        // Xử lý các vi phạm liên quan (ví dụ: tự động giải quyết)
        for (StudentViolation relatedViolation : relatedViolations) {
            relatedViolation.setStatus(status); // Sử dụng cùng trạng thái với vi phạm chính
            relatedViolation.setHandledBy(admin);
            relatedViolation.setActionTaken(
                    String.format("Tự động xử lý cùng với vi phạm ID %d: %s",
                            violationId, request.getActionTaken())
            );
            relatedViolation.setResolvedAt(LocalDateTime.now());
        }

        // Lưu vi phạm chính và các vi phạm liên quan
        studentViolationRepository.save(violation);
        studentViolationRepository.saveAll(relatedViolations);

        return violation;
    }

    @Override
    @Transactional(readOnly = true)
    public ViolationSummaryDTO getViolationSummary(String classifyBy, String statusFilter, Integer entityId, int pageNo, int pageSize, String sortBy, String sortDir) {
        if (!"FlashcardSet".equalsIgnoreCase(classifyBy) && !"Rating".equalsIgnoreCase(classifyBy)) {
            throw new RuntimeException("classifyBy phải là 'FlashcardSet' hoặc 'Rating'.");
        }

        if (entityId == null) {
            throw new RuntimeException("entityId là bắt buộc.");
        }

        ViolationStatus violationStatus = null;
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            try {
                violationStatus = ViolationStatus.valueOf(statusFilter.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Giá trị status không hợp lệ. Phải là PENDING, RESOLVED hoặc DISMISSED.");
            }
        }

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<StudentViolation> violationPage;
        long totalViolations;
        if ("FlashcardSet".equalsIgnoreCase(classifyBy)) {
            violationPage = studentViolationRepository.findByStudentFlashcardSetId(entityId, violationStatus, pageable);
            totalViolations = studentViolationRepository.countByStudentFlashcardSetId(entityId);
        } else {
            violationPage = studentViolationRepository.findByStudentCourseRatingId(entityId, violationStatus, pageable);
            totalViolations = studentViolationRepository.countByStudentCourseRatingId(entityId);
        }

        List<ViolationSummaryDTO.ViolationDescriptionDTO> descriptions = violationPage.getContent().stream()
                .map(v -> {
                    ViolationSummaryDTO.ViolationDescriptionDTO descDTO = new ViolationSummaryDTO.ViolationDescriptionDTO();
                    descDTO.setViolationId(v.getViolationId());
                    descDTO.setDescription(v.getDescription());
                    descDTO.setStatus(v.getStatus().name());
                    descDTO.setCreatedAt(v.getCreatedAt());
                    return descDTO;
                })
                .collect(Collectors.toList());

        PageResponseDTO<ViolationSummaryDTO.ViolationDescriptionDTO> descriptionsPage = PageResponseDTO
                .<ViolationSummaryDTO.ViolationDescriptionDTO>builder()
                .content(descriptions)
                .pageNo(violationPage.getNumber())
                .pageSize(violationPage.getSize())
                .totalElements(violationPage.getTotalElements())
                .totalPages(violationPage.getTotalPages())
                .last(violationPage.isLast())
                .build();

        ViolationSummaryDTO dto = new ViolationSummaryDTO();
        if ("FlashcardSet".equalsIgnoreCase(classifyBy)) {
            dto.setStudentSetId(entityId);
        } else {
            dto.setRatingId(entityId);
        }
        dto.setTotalViolations(totalViolations);
        dto.setDescriptions(descriptionsPage);

        return dto;
    }

    private List<StudentViolation> findRelatedPendingViolations(StudentViolation violation) {
        // Tìm các vi phạm PENDING khác cho cùng student_set_id hoặc rating_id
        if (violation.getStudentFlashcardSet() != null) {
            return studentViolationRepository.findByStudentFlashcardSetStudentSetIdAndStatusAndViolationIdNot(
                    violation.getStudentFlashcardSet().getStudentSetId(),
                    ViolationStatus.PENDING,
                    violation.getViolationId()
            );
        } else if (violation.getStudentCourseRating() != null) {
            return studentViolationRepository.findByStudentCourseRatingRatingIdAndStatusAndViolationIdNot(
                    violation.getStudentCourseRating().getRatingId(),
                    ViolationStatus.PENDING,
                    violation.getViolationId()
            );
        }
        return List.of();
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
