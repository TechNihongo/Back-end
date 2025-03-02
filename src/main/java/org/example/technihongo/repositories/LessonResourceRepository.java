package org.example.technihongo.repositories;

import org.example.technihongo.entities.LessonResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonResourceRepository extends JpaRepository<LessonResource, Integer> {
    void deleteByLesson_LessonIdIn(List<Integer> lessonIds);
    Integer countByLesson_LessonId(Integer lessonId);
    LessonResource findByLessonResourceId(Integer lessonResourceId);
    List<LessonResource> findByLesson_LessonIdOrderByTypeOrderAsc(Integer lessonId);
    Boolean existsByLesson_LessonIdAndLearningResource_ResourceId(Integer lessonId, Integer resourceId);
    Boolean existsByLesson_LessonIdAndSystemFlashCardSet_SystemSetId(Integer lessonId, Integer setId);
    Boolean existsByLesson_LessonIdAndQuiz_QuizId(Integer lessonId, Integer quizId);
    Boolean existsByLearningResource_ResourceId(Integer resourceId);
}
