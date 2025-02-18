package org.example.technihongo.repositories;

import jakarta.validation.constraints.NotNull;
import org.example.technihongo.entities.CourseStudyPlan;
import org.example.technihongo.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    Lesson findByLessonId(Integer lessonId);
    List<Lesson> findByCourseStudyPlan_CoursePlanId(Integer coursePlanId);
    void deleteByCourseStudyPlan_CoursePlanId(Integer coursePlanId);
    Integer countLessonByCourseStudyPlan(@NotNull CourseStudyPlan courseStudyPlan);
    List<Lesson> findByCourseStudyPlan_CoursePlanIdOrderByLessonOrderAsc(Integer coursePlanId);

}
