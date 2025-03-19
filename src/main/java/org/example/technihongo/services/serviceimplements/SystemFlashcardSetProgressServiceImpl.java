//package org.example.technihongo.services.serviceimplements;
//
//import org.example.technihongo.dto.FlashcardSetProgressDTO;
//import org.example.technihongo.entities.StudentFlashcardSet;
//import org.example.technihongo.entities.StudentFlashcardSetProgress;
//import org.example.technihongo.repositories.StudentFlashcardProgressRepository;
//import org.example.technihongo.repositories.StudentFlashcardSetProgressRepository;
//import org.example.technihongo.repositories.StudentFlashcardSetRepository;
//import org.example.technihongo.services.interfaces.SystemFlashcardSetProgressService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//public class SystemFlashcardSetProgressServiceImpl implements SystemFlashcardSetProgressService {
//
//    @Autowired
//    private StudentFlashcardSetProgressRepository setProgressRepo;
//    @Autowired
//    private StudentFlashcardSetRepository studentSetRepo;
//    @Autowired
//    private StudentFlashcardProgressRepository flashcardProgressRepo;
//
//    @Override
//    public List<FlashcardSetProgressDTO> getAllSystemSetProgress(Integer studentId) {
//        List<StudentFlashcardSetProgress> progresses = setProgressRepo.findByStudentStudentId(studentId)
//                .stream()
//                .filter(p -> p.getSystemFlashcardSet().getSystemSetId() != null)
//                .toList();
//        return progresses.stream().map(this::mapToDTO).collect(Collectors.toList());
//    }
//
//    @Override
//    public FlashcardSetProgressDTO getSystemSetProgress(Integer studentId, Integer systemSetId) {
//        return null;
//    }
//    private FlashcardSetProgressDTO mapToDTO(StudentFlashcardSetProgress progress) {
//        StudentFlashcardSet set = studentSetRepo.findById(progress.getStudentFlashcardSet().getStudentSetId()).orElseThrow();
//        int cardStudied = flashcardProgressRepo.findByStudentStudentIdAndFlashcard_SystemFlashCardSet_SystemSetId(
//                        progress.getSystemFlashcardSet().getSystemSetId(), set.getStudentSetId())
//                .stream().filter(StudentFlashcardProgress::getIsLearned).count();
//        return FlashcardSetProgressDTO.builder()
//                .setId(set.getStudentSetId())
//                .title(set.getTitle())
//                .totalCards(set.getTotalCards())
//                .cardStudied(cardStudied)
//                .completionStatus(progress.getCompletionStatus().toString())
//                .lastStudied(progress.getLastStudied())
//                .studyCount(progress.getStudyCount())
//                .setType("STUDENT")
//                .build();
//    }
//}
