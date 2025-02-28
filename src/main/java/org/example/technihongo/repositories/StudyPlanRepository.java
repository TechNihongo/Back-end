package org.example.technihongo.repositories;

import org.example.technihongo.entities.Course;
import org.example.technihongo.entities.StudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyPlanRepository extends JpaRepository<StudyPlan, Integer> {
    StudyPlan findByStudyPlanId(Integer id);
    List<StudyPlan> findByCourse_CourseId(Integer courseId);
    Integer countByCourse_CourseId(Integer courseId);
}

