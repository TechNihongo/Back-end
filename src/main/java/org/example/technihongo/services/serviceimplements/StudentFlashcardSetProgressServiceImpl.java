package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.dto.FlashcardSetProgressDTO;
import org.example.technihongo.entities.StudentFlashcardProgress;
import org.example.technihongo.entities.StudentFlashcardSet;
import org.example.technihongo.entities.StudentFlashcardSetProgress;
import org.example.technihongo.entities.SystemFlashcardSet;
import org.example.technihongo.enums.CompletionStatus;
import org.example.technihongo.repositories.StudentFlashcardProgressRepository;
import org.example.technihongo.repositories.StudentFlashcardSetProgressRepository;
import org.example.technihongo.repositories.StudentFlashcardSetRepository;
import org.example.technihongo.services.interfaces.StudentFlashcardSetProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
