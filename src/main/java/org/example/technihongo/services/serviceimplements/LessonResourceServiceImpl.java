package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CreateLessonResourceDTO;
import org.example.technihongo.dto.UpdateLessonResourceDTO;
import org.example.technihongo.dto.UpdateLessonResourceOrderDTO;
import org.example.technihongo.entities.*;
import org.example.technihongo.repositories.LessonRepository;
import org.example.technihongo.repositories.LessonResourceRepository;
import org.example.technihongo.repositories.QuizRepository;
import org.example.technihongo.repositories.SystemFlashcardSetRepository;
import org.example.technihongo.repositories.LearningResourceRepository;
import org.example.technihongo.services.interfaces.LessonResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Component
public class LessonResourceServiceImpl implements LessonResourceService {
    @Autowired
    private LessonResourceRepository lessonResourceRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private LearningResourceRepository learningResourceRepository;
    @Autowired
    private SystemFlashcardSetRepository systemFlashcardSetRepository;
    @Autowired
    private QuizRepository quizRepository;

    @Override
    public List<LessonResource> getLessonResourceListByLessonId(Integer lessonId) {
        lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson ID not found"));

        return lessonResourceRepository.findByLesson_LessonIdOrderByTypeOrderAsc(lessonId);
    }

    @Override
    public List<LessonResource> getActiveLessonResourceListByLessonId(Integer lessonId) {
        lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson ID not found"));

        return lessonResourceRepository.findByLesson_LessonIdOrderByTypeOrderAsc(lessonId).stream()
                .filter(LessonResource::isActive)
                .toList();
    }

    @Override
    public LessonResource getLessonResourceById(Integer lessonResourceId) {
        return lessonResourceRepository.findById(lessonResourceId)
                .orElseThrow(() -> new RuntimeException("LessonResource ID not found"));
    }

    @Override
    public LessonResource getActiveLessonResourceById(Integer lessonResourceId) {
        return lessonResourceRepository.findById(lessonResourceId)
                .filter(LessonResource::isActive)
                .orElseThrow(() -> new RuntimeException("LessonResource ID not found"));    }

    @Override
    public LessonResource createLessonResource(CreateLessonResourceDTO createLessonResourceDTO) {
        lessonRepository.findById(createLessonResourceDTO.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson ID not found"));

        Integer l = createLessonResourceDTO.getResourceId();
        Integer s = createLessonResourceDTO.getSystemSetId();
        Integer q = createLessonResourceDTO.getQuizId();

        if(l == null && s == null && q == null){
            throw new RuntimeException("Content ID not found!");
        }

        if((l != null && s != null) || (l != null && q != null) || (q != null && s != null)){
            throw new RuntimeException("Only one content can be created!");
        }

        LearningResource learningResource = learningResourceRepository.findByResourceId(l);
        if(l != null && learningResource == null){
            throw new RuntimeException("LearningResource ID not found!!");
        }

        SystemFlashcardSet systemFlashcardSet = systemFlashcardSetRepository.findBySystemSetId(s);
        if(s != null && systemFlashcardSet == null){
            throw new RuntimeException("SystemFlashcardSet ID not found!!");
        }

        Quiz quiz = quizRepository.findByQuizId(q);
        if(q != null && quiz == null){
            throw new RuntimeException("Quiz ID not found!!");
        }

        Lesson lesson = lessonRepository.findByLessonId(createLessonResourceDTO.getLessonId());
        if(l != null && lessonResourceRepository.existsByLesson_LessonIdAndLearningResource_ResourceId(
                lesson.getLessonId(), l)){
            throw new RuntimeException("This LearningResource already exists in lesson!");
        }

        if(s != null && lessonResourceRepository.existsByLesson_LessonIdAndSystemFlashCardSet_SystemSetId(
                lesson.getLessonId(), s)){
            throw new RuntimeException("This SystemFlashcardSet already exists in lesson!");
        }

        if(q != null && lessonResourceRepository.existsByLesson_LessonIdAndQuiz_QuizId(
                lesson.getLessonId(), q)){
            throw new RuntimeException("This Quiz already exists in lesson!");
        }

        LessonResource lessonResource;
        if(learningResource != null){
            lessonResource = lessonResourceRepository.save(LessonResource.builder()
                    .lesson(lesson)
                    .type("Resource")
                    .typeOrder(lessonResourceRepository.countByLesson_LessonId(lesson.getLessonId()) + 1)
                    .learningResource(learningResource)
                    .build());
        }
        else if(systemFlashcardSet != null){
            lessonResource = lessonResourceRepository.save(LessonResource.builder()
                    .lesson(lesson)
                    .type("FlashcardSet")
                    .typeOrder(lessonResourceRepository.countByLesson_LessonId(lesson.getLessonId()) + 1)
                    .systemFlashCardSet(systemFlashcardSet)
                    .build());
        }
        else {
            lessonResource = lessonResourceRepository.save(LessonResource.builder()
                    .lesson(lesson)
                    .type("Quiz")
                    .typeOrder(lessonResourceRepository.countByLesson_LessonId(lesson.getLessonId()) + 1)
                    .quiz(quiz)
                    .build());
        }

        return lessonResource;
    }

    @Override
    public void updateLessonResource(Integer lessonResourceId, UpdateLessonResourceDTO updateLessonResourceDTO) {
        lessonResourceRepository.findById(lessonResourceId)
                .orElseThrow(() -> new RuntimeException("LessonResource ID not found"));

        LessonResource lessonResource = lessonResourceRepository.findByLessonResourceId(lessonResourceId);
        lessonResource.setActive(updateLessonResourceDTO.getIsActive());
        lessonResourceRepository.save(lessonResource);
    }

    @Override
    public void updateLessonResourceOrder(Integer lessonId, UpdateLessonResourceOrderDTO updateLessonResourceOrderDTO) {
        lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson ID not found"));

        List<LessonResource> lessonResourceList = lessonResourceRepository.findByLesson_LessonIdOrderByTypeOrderAsc(lessonId);
        List<Integer> newOrder = updateLessonResourceOrderDTO.getNewLessonResourceOrder();

        if (lessonResourceList.size() != newOrder.size()) {
            throw new IllegalArgumentException("LessonResource count does not match newOrder!");
        }

        for (int i = 0; i < lessonResourceList.size(); i++) {
            lessonResourceList.get(i).setTypeOrder(newOrder.get(i));
        }

        lessonResourceRepository.saveAll(lessonResourceList);

    }

    @Override
    public void deleteLessonResource(Integer lessonResourceId) {
        LessonResource deletedLessonResource = lessonResourceRepository.findById(lessonResourceId)
                .orElseThrow(() -> new RuntimeException("LessonResource ID not found"));

        if(deletedLessonResource.isActive()){
            throw new RuntimeException("Cannot delete an active LessonResource!");
        }

        Integer lessonId = deletedLessonResource.getLesson().getLessonId();
        Integer deletedOrder = deletedLessonResource.getTypeOrder();

        lessonResourceRepository.delete(deletedLessonResource);

        List<LessonResource> lessonResources = lessonResourceRepository.findByLesson_LessonIdOrderByTypeOrderAsc(lessonId);
        for (LessonResource lessonResource : lessonResources) {
            if (lessonResource.getTypeOrder() > deletedOrder) {
                lessonResource.setTypeOrder(lessonResource.getTypeOrder() - 1);
            }
        }

        lessonResourceRepository.saveAll(lessonResources);
    }
}
