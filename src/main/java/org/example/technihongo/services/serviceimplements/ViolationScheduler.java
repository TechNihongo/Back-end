package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.entities.StudentFlashcardSet;
import org.example.technihongo.entities.StudentViolation;
import org.example.technihongo.enums.ViolationStatus;
import org.example.technihongo.repositories.StudentFlashcardSetRepository;
import org.example.technihongo.repositories.StudentViolationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ViolationScheduler {
    private final StudentViolationRepository studentViolationRepository;
    private final StudentFlashcardSetRepository studentFlashcardSetRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Chạy hàng ngày lúc 00:00
    @Transactional
    public void checkUneditedFlashcardSets() {
        // Tìm các vi phạm RESOLVED với violationHandledAt > 1 ngày
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        List<StudentViolation> violations = studentViolationRepository.findByStatusAndViolationHandledAtBefore(
                ViolationStatus.RESOLVED, oneDayAgo);

        for (StudentViolation violation : violations) {
            StudentFlashcardSet flashcardSet = violation.getStudentFlashcardSet();
            if (flashcardSet != null && !flashcardSet.isDeleted() && !flashcardSet.isPublic()) {
                // Kiểm tra xem flashcardSet có được chỉnh sửa không
                if (flashcardSet.getUpdatedAt() == null || flashcardSet.getUpdatedAt().isBefore(violation.getViolationHandledAt())) {
                    // Giữ isPublic = false vĩnh viễn
                    flashcardSet.setPublic(false);
                    studentFlashcardSetRepository.save(flashcardSet);
                }
            }
        }
    }
}