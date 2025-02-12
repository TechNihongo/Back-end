package org.example.technihongo.repositories;

import org.example.technihongo.entities.CourseStudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseStudyPlanRepository extends JpaRepository<CourseStudyPlan, Integer> {
    CourseStudyPlanRepository findByCoursePlanId(Integer id);
}

