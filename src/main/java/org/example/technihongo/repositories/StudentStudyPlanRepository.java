package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentStudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentStudyPlanRepository extends JpaRepository<StudentStudyPlan, Integer> {
    boolean existsByStudyPlan_StudyPlanId(Integer studyPlanId);
}
