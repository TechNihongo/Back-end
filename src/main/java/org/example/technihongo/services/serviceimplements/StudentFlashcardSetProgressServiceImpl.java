package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.dto.FlashcardSetProgressDTO;
import org.example.technihongo.entities.*;
import org.example.technihongo.enums.CompletionStatus;
import org.example.technihongo.repositories.*;
import org.example.technihongo.services.interfaces.StudentFlashcardSetProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentFlashcardSetProgressServiceImpl implements StudentFlashcardSetProgressService {
    @Autowired
    private StudentFlashcardSetProgressRepository setProgressRepo;
    @Autowired
    private StudentFlashcardSetRepository studentSetRepo;
    @Autowired
    private StudentFlashcardProgressRepository flashcardProgressRepo;
    @Autowired
    private SystemFlashcardSetRepository systemSetRepo;
    @Autowired
    private StudentRepository studentRepo;
    @Autowired
    private FlashcardRepository flashcardRepo;
    @Autowired
    private StudentDailyLearningLogRepository dailyLogRepository;

    @Override
    public List<FlashcardSetProgressDTO> getAllStudentAndSystemSetProgress(Integer studentId) {
        List<StudentFlashcardSetProgress> progresses = setProgressRepo.findByStudentStudentId(studentId);
        return progresses.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FlashcardSetProgressDTO getStudentOrSystemSetProgress(Integer studentId, Integer setId, boolean isSystemSet) {
        if (!isSystemSet) {
            // Lấy tiến độ của StudentFlashcardSet
            StudentFlashcardSetProgress progress = setProgressRepo
                    .findByStudentStudentIdAndStudentFlashcardSet_StudentSetId(studentId, setId)
                    .orElseThrow(() -> new RuntimeException("Student set progress not found for student ID " + studentId + " and set ID " + setId));
            return mapToDTO(progress);
        } else {
            // Lấy tiến độ của SystemFlashcardSet
            StudentFlashcardSetProgress progress = setProgressRepo
                    .findByStudentStudentIdAndSystemFlashcardSet_SystemSetId(studentId, setId)
                    .orElseThrow(() -> new RuntimeException("System set progress not found for student ID " + studentId + " and set ID " + setId));
            return mapToDTO(progress);
        }
    }

    @Override
    public void trackFlashcardSetProgress(Integer studentId, Integer setId, boolean isSystemSet, Integer currentFlashcardId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID " + studentId));

        Optional<StudentFlashcardSetProgress> existingProgressOpt = isSystemSet
                ? setProgressRepo.findByStudentStudentIdAndSystemFlashcardSet_SystemSetId(studentId, setId)
                : setProgressRepo.findByStudentStudentIdAndStudentFlashcardSet_StudentSetId(studentId, setId);

        StudentFlashcardSetProgress progress;
        if (existingProgressOpt.isEmpty()) {
            // Tạo mới tiến độ nếu chưa tồn tại
            progress = new StudentFlashcardSetProgress();
            progress.setStudent(student);
            progress.setCompletionStatus(CompletionStatus.IN_PROGRESS);
            progress.setCardStudied(0);
            progress.setStudyCount(1);
            progress.setLastStudied(LocalDateTime.now());

            if (isSystemSet) {
                SystemFlashcardSet systemSet = systemSetRepo.findById(setId)
                        .orElseThrow(() -> new RuntimeException("SystemFlashcardSet not found with ID " + setId));
                progress.setSystemFlashcardSet(systemSet);
            } else {
                StudentFlashcardSet studentSet = studentSetRepo.findById(setId)
                        .orElseThrow(() -> new RuntimeException("StudentFlashcardSet not found with ID " + setId));
                progress.setStudentFlashcardSet(studentSet);
            }

            // Xử lý currentFlashcardId khi tạo mới
            if (currentFlashcardId != null) {
                Flashcard currentCard = flashcardRepo.findById(currentFlashcardId)
                        .orElseThrow(() -> new RuntimeException("Flashcard not found with ID " + currentFlashcardId));
                // Kiểm tra flashcard có thuộc bộ không
                if (isSystemSet && currentCard.getSystemFlashCardSet() != null && currentCard.getSystemFlashCardSet().getSystemSetId().equals(setId)) {
                    progress.setCurrentFlashCardId(currentCard);
                } else if (!isSystemSet && currentCard.getStudentFlashCardSet() != null && currentCard.getStudentFlashCardSet().getStudentSetId().equals(setId)) {
                    progress.setCurrentFlashCardId(currentCard);
                } else {
                    //logger.warn("Flashcard ID {} does not belong to set ID {} (isSystemSet: {})", currentFlashcardId, setId, isSystemSet);
                    progress.setCurrentFlashCardId(null);
                }
            } else {
                // Nếu không truyền currentFlashcardId, thử lấy thẻ đầu tiên
                Optional<Flashcard> firstCardOpt = isSystemSet
                        ? flashcardRepo.findTopBySystemFlashCardSet_SystemSetIdOrderByCardOrderAsc(setId)
                        : flashcardRepo.findTopByStudentFlashCardSet_StudentSetIdOrderByCardOrderAsc(setId);
                if (firstCardOpt.isPresent()) {
                    progress.setCurrentFlashCardId(firstCardOpt.get());
                } else {
                    //logger.warn("No flashcards found in set ID {} (isSystemSet: {}). Setting currentFlashcardId to null.", setId, isSystemSet);
                    progress.setCurrentFlashCardId(null);
                }
            }
        } else {
            // Cập nhật tiến độ hiện có
            progress = existingProgressOpt.get();

            // Tính lại cardStudied dựa trên isLearned
            long cardStudied = isSystemSet
                    ? flashcardProgressRepo.findByStudentStudentIdAndFlashcard_SystemFlashCardSet_SystemSetId(studentId, setId)
                    .stream().filter(StudentFlashcardProgress::isLearned).count()
                    : flashcardProgressRepo.findByStudentStudentIdAndFlashcard_StudentFlashCardSet_StudentSetId(studentId, setId)
                    .stream().filter(StudentFlashcardProgress::isLearned).count();

            progress.setCardStudied((int) cardStudied);
            progress.setLastStudied(LocalDateTime.now());
            progress.setStudyCount(progress.getStudyCount() + 1);

            // Lấy totalCards từ bộ tương ứng
            int totalCards = isSystemSet
                    ? progress.getSystemFlashcardSet().getTotalCards()
                    : progress.getStudentFlashcardSet().getTotalCards();

            // Kiểm tra hoàn thành
            if (cardStudied >= totalCards && progress.getCompletionStatus() != CompletionStatus.COMPLETED) {
                progress.setCompletionStatus(CompletionStatus.COMPLETED);
                StudentDailyLearningLog dailyLog = dailyLogRepository.findByStudentStudentIdAndLogDate(studentId, LocalDate.now()).get();
                dailyLog.setCompletedFlashcardSets(dailyLog.getCompletedFlashcardSets() + 1);
                dailyLogRepository.save(dailyLog);
            }

            // Cập nhật currentFlashcardId theo tham số truyền vào
            if (currentFlashcardId != null) {
                Flashcard currentCard = flashcardRepo.findById(currentFlashcardId)
                        .orElseThrow(() -> new RuntimeException("Flashcard not found with ID " + currentFlashcardId));
                // Kiểm tra flashcard có thuộc bộ không
                if (isSystemSet && currentCard.getSystemFlashCardSet() != null && currentCard.getSystemFlashCardSet().getSystemSetId().equals(setId)) {
                    progress.setCurrentFlashCardId(currentCard);
                } else if (!isSystemSet && currentCard.getStudentFlashCardSet() != null && currentCard.getStudentFlashCardSet().getStudentSetId().equals(setId)) {
                    progress.setCurrentFlashCardId(currentCard);
                }
            } else if (progress.getCurrentFlashCardId() == null) {
                // Nếu không truyền currentFlashcardId và hiện tại là null, thử lấy thẻ đầu tiên
                Optional<Flashcard> firstCardOpt = isSystemSet
                        ? flashcardRepo.findTopBySystemFlashCardSet_SystemSetIdOrderByCardOrderAsc(setId)
                        : flashcardRepo.findTopByStudentFlashCardSet_StudentSetIdOrderByCardOrderAsc(setId);
                firstCardOpt.ifPresent(progress::setCurrentFlashCardId);
            }
        }

        setProgressRepo.save(progress);
    }

    private FlashcardSetProgressDTO mapToDTO(StudentFlashcardSetProgress progress) {
        if (progress.getStudentFlashcardSet() != null) {
            // Trường hợp StudentFlashcardSet
            StudentFlashcardSet set = progress.getStudentFlashcardSet();
            long cardStudied = flashcardProgressRepo
                    .findByStudentStudentIdAndFlashcard_StudentFlashCardSet_StudentSetId(
                            progress.getStudent().getStudentId(), set.getStudentSetId())
                    .stream()
                    .filter(StudentFlashcardProgress::isLearned)
                    .count();

            return FlashcardSetProgressDTO.builder()
                    .progressId(progress.getProgressId())
                    .setId(set.getStudentSetId())
                    .title(set.getTitle())
                    .totalCards(set.getTotalCards())
                    .cardStudied((int) cardStudied)
                    .lastStudied(progress.getLastStudied())
                    .studyCount(progress.getStudyCount())
                    .completionStatus(CompletionStatus.valueOf(progress.getCompletionStatus().toString()))
                    .isSystemSet(false) // StudentFlashcardSet
                    .build();
        } else if (progress.getSystemFlashcardSet() != null) {
            // Trường hợp SystemFlashcardSet
            SystemFlashcardSet set = progress.getSystemFlashcardSet();
            long cardStudied = flashcardProgressRepo
                    .findByStudentStudentIdAndFlashcard_SystemFlashCardSet_SystemSetId(
                            progress.getStudent().getStudentId(), set.getSystemSetId())
                    .stream()
                    .filter(StudentFlashcardProgress::isLearned)
                    .count();

            return FlashcardSetProgressDTO.builder()
                    .progressId(progress.getProgressId())
                    .setId(set.getSystemSetId())
                    .title(set.getTitle())
                    .totalCards(set.getTotalCards())
                    .cardStudied((int) cardStudied)
                    .lastStudied(progress.getLastStudied())
                    .studyCount(progress.getStudyCount())
                    .completionStatus(CompletionStatus.valueOf(progress.getCompletionStatus().toString()))
                    .isSystemSet(true) // SystemFlashcardSet
                    .build();
        } else {
            throw new RuntimeException("Progress is not associated with any FlashcardSet");
        }
    }
}
