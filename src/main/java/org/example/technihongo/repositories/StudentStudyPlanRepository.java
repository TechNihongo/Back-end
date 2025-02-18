package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentStudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentStudyPlanRepository extends JpaRepository<StudentStudyPlan, Integer> {
    boolean existsByCourseStudyPlan_CoursePlanId(Integer coursePlanId);
}
