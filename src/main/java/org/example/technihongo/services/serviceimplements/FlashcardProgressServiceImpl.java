package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.dto.FlashcardProgressDTO;
import org.example.technihongo.entities.Flashcard;
import org.example.technihongo.entities.Student;
import org.example.technihongo.entities.StudentFlashcardProgress;
import org.example.technihongo.repositories.FlashcardRepository;
import org.example.technihongo.repositories.StudentFlashcardProgressRepository;
import org.example.technihongo.services.interfaces.FlashcardProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FlashcardProgressServiceImpl implements FlashcardProgressService {

    @Autowired
    private StudentFlashcardProgressRepository studentFlashcardProgressRepository;

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Override
    public List<FlashcardProgressDTO> getStarredFlashcards(Integer studentId, Integer setId, boolean isSystemSet) {
        List<StudentFlashcardProgress> progresses = isSystemSet
                ? studentFlashcardProgressRepository.findByStudentStudentIdAndFlashcard_SystemFlashCardSet_SystemSetIdAndStarred(studentId, setId, true)
                : studentFlashcardProgressRepository.findByStudentStudentIdAndFlashcard_StudentFlashCardSet_StudentSetIdAndStarred(studentId, setId, true);

        return progresses.stream().map(progress -> {
            Flashcard flashcard = progress.getFlashcard();
            return FlashcardProgressDTO.builder()
                    .flashcardId(flashcard.getFlashCardId())
                    .japaneseDefinition(flashcard.getDefinition())
                    .vietEngTranslation(flashcard.getTranslation())
                    .isLearned(progress.isLearned())
                    .lastStudied(progress.getLastStudied())
                    .setType(isSystemSet ? "SYSTEM" : "STUDENT")
                    .starred(progress.getStarred())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public void updateFlashcardProgress(Integer studentId, Integer flashcardId, Boolean starred) {
        StudentFlashcardProgress progress = studentFlashcardProgressRepository.findByStudentStudentIdAndFlashcardFlashCardId(studentId, flashcardId)
                .orElseGet(() -> {
                    Student student = new Student();
                    student.setStudentId(studentId);
                    return StudentFlashcardProgress.builder()
                            .student(student)
                            .flashcard(flashcardRepository.findById(flashcardId).orElseThrow(() -> new RuntimeException("Flashcard not found")))
                            .createdAt(LocalDateTime.now())
                            .build();
                });
        if(starred != null) {
            progress.setStarred(starred);
        }
        progress.setLearned(true);
        progress.setLastStudied(LocalDateTime.now());
        studentFlashcardProgressRepository.save(progress);
    }
}
