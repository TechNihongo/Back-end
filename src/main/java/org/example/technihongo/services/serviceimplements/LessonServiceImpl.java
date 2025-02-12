package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.entities.Lesson;
import org.example.technihongo.repositories.LessonRepository;
import org.example.technihongo.services.interfaces.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Component
public class LessonServiceImpl implements LessonService {
    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public Optional<Lesson> getLessonById(Integer lessonId) {
        return Optional.ofNullable(lessonRepository.findByLessonId(lessonId));
    }

    @Override
    public List<Lesson> getLessonListByCourseStudyPlanId(Integer coursePlanId) {
        return lessonRepository.findAll().stream()
                .filter(l -> l.getCourseStudyPlan().getCoursePlanId().equals(coursePlanId))
                .toList();
    }
}
