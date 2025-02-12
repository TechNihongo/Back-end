package org.example.technihongo.services.interfaces;

import org.example.technihongo.entities.StudyPlan;

import java.util.List;
import java.util.Optional;

public interface StudyPlanService {
    List<StudyPlan> studyPlanList();
    Optional<StudyPlan> getStudyPlan(Integer studyPlanId);
}
