package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyPlanRepository extends JpaRepository<StudyPlan, Integer> {
    StudyPlan findByStudyPlanId(Integer id);
}
