package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CreateLessonDTO;
import org.example.technihongo.dto.UpdateLessonDTO;
import org.example.technihongo.dto.UpdateLessonOrderDTO;
import org.example.technihongo.entities.CourseStudyPlan;
import org.example.technihongo.entities.Lesson;
import org.example.technihongo.repositories.CourseStudyPlanRepository;
import org.example.technihongo.repositories.LessonRepository;
import org.example.technihongo.services.interfaces.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Component
public class LessonServiceImpl implements LessonService {
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CourseStudyPlanRepository courseStudyPlanRepository;

    @Override
    public Optional<Lesson> getLessonById(Integer lessonId) {
        return Optional.ofNullable(lessonRepository.findByLessonId(lessonId));
    }

    @Override
    public List<Lesson> getLessonListByCourseStudyPlanId(Integer coursePlanId) {
        return lessonRepository.findByCourseStudyPlan_CoursePlanIdOrderByLessonOrderAsc(coursePlanId);
    }

    @Override
    public Lesson createLesson(CreateLessonDTO createLessonDTO) {
        CourseStudyPlan csp = courseStudyPlanRepository.findByCoursePlanId(createLessonDTO.getCoursePlanId());
        if(csp == null){
            throw new RuntimeException("CourseStudyPlan ID not found!");
        }

        Lesson lesson = lessonRepository.save(Lesson.builder()
                .courseStudyPlan(csp)
                .title(createLessonDTO.getTitle())
                .lessonOrder(lessonRepository.countLessonByCourseStudyPlan(csp) + 1)
                .build());

        return lesson;
    }

    @Override
    public void updateLesson(Integer lessonId, UpdateLessonDTO updateLessonDTO) {
        if(lessonRepository.findByLessonId(lessonId) == null){
            throw new RuntimeException("Lesson ID not found!");
        }

        Lesson lesson = lessonRepository.findByLessonId(lessonId);
        lesson.setTitle(updateLessonDTO.getTitle());
        lesson.setUpdatedAt(LocalDateTime.now());

        lessonRepository.save(lesson);
    }

    @Override
    @Transactional
    public void updateLessonOrder(Integer coursePlanId, UpdateLessonOrderDTO updateLessonOrderDTO) {
        if(courseStudyPlanRepository.findByCoursePlanId(coursePlanId) == null){
            throw new RuntimeException("CourseStudyPlan ID not found!");
        }

        List<Lesson> lessons = lessonRepository.findByCourseStudyPlan_CoursePlanIdOrderByLessonOrderAsc(coursePlanId);
        List<Integer> newOrder = updateLessonOrderDTO.getNewLessonOrder();

        if (lessons.size() != newOrder.size()) {
            throw new IllegalArgumentException("Lesson count does not match newOrder!");
        }

        for (int i = 0; i < lessons.size(); i++) {
            lessons.get(i).setLessonOrder(newOrder.get(i));
        }

        lessonRepository.saveAll(lessons);
    }
}
