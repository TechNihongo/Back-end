package org.example.technihongo.repositories;

import org.example.technihongo.entities.Course;
import org.example.technihongo.entities.CourseStudyPlan;
import org.example.technihongo.entities.StudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseStudyPlanRepository extends JpaRepository<CourseStudyPlan, Integer> {
    CourseStudyPlan findByCoursePlanId(Integer id);
    boolean existsByCourseAndStudyPlan(Course course, StudyPlan studyPlan);
    List<CourseStudyPlan> findByCourse_CourseId(Integer courseId);
    boolean existsByStudyPlan(StudyPlan studyPlan);
}

