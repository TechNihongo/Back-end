//package org.example.technihongo.services.serviceimplements;
//
//import org.example.technihongo.dto.FlashcardSetProgressDTO;
//import org.example.technihongo.entities.StudentFlashcardSet;
//import org.example.technihongo.entities.StudentFlashcardSetProgress;
//import org.example.technihongo.enums.CompletionStatus;
//import org.example.technihongo.repositories.StudentFlashcardProgressRepository;
//import org.example.technihongo.repositories.StudentFlashcardSetProgressRepository;
//import org.example.technihongo.repositories.StudentFlashcardSetRepository;
//import org.example.technihongo.services.interfaces.StudentFlashcardSetProgressService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//public class StudentFlashcardSetProgressServiceImpl implements StudentFlashcardSetProgressService {
//
//    @Autowireds
//    private StudentFlashcardSetProgressRepository setProgressRepo;
//    @Autowired
//    private StudentFlashcardSetRepository studentSetRepo;
//    @Autowired
//    private StudentFlashcardProgressRepository flashcardProgressRepo;
//
//    @Override
//    public List<FlashcardSetProgressDTO> getAllStudentSetProgress(Integer studentId) {
//        List<StudentFlashcardSetProgress> progresses = setProgressRepo.findByStudentStudentId(studentId)
//                .stream()
//                .filter(p -> p.getStudentFlashcardSet().getStudentSetId() != null)
//                .toList();
//        return progresses.stream().map(this::mapToDTO).collect(Collectors.toList());
//    }
//
//    @Override
//    public FlashcardSetProgressDTO getStudentSetProgress(Integer studentId, Integer studentSetId) {
//        StudentFlashcardSetProgress progress = setProgressRepo.findByStudentStudentIdAndStudentFlashcardSet_StudentSetId(studentId, studentSetId)
//                .orElseThrow(() -> new RuntimeException("Student set progress not found"));
//        return mapToDTO(progress);
//    }
//
//    private FlashcardSetProgressDTO mapToDTO(StudentFlashcardSetProgress progress) {
//        StudentFlashcardSet set = progress.getStudentFlashcardSet();
//        int cardStudied = flashcardProgressRepo.findByStudentStudentIdAndFlashcard_StudentFlashCardSet_StudentSetId(
//                        progress.getStudent().getStudentId(), set.getStudentSetId())
//                .stream().filter(StudentFlashcardProgress::getIsLearned).count();
//
//        return FlashcardSetProgressDTO.builder()
//                .setId(set.getStudentSetId())
//                .title(set.getTitle())
//                .totalCards(set.getTotalCards())
//                .cardStudied(cardStudied)
//                .completionStatus(progress.getCompletionStatus().toString())
//                .lastStudied(progress.getLastStudied())
//                .studyCount(progress.getStudyCount())
//                .completableStatus(CompletionStatus.valueOf(progress.getCompletionStatus().toString()))
//                .setType("STUDENT")
//                .build();
//    }
//}
