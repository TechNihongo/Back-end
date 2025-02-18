package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.CreateStudyPlanDTO;
import org.example.technihongo.dto.UpdateStudyPlanDTO;
import org.example.technihongo.entities.StudyPlan;

import java.util.List;
import java.util.Optional;

public interface StudyPlanService {
    List<StudyPlan> studyPlanList();
    Optional<StudyPlan> getStudyPlan(Integer studyPlanId);
    List<StudyPlan> getActiveStudyPlansByCourseId(Integer courseId);
    StudyPlan createStudyPlan(CreateStudyPlanDTO createStudyPlanDTO);
    void updateStudyPlan(Integer planId, UpdateStudyPlanDTO updateStudyPlanDTO);
    void deleteStudyPlan(Integer studyPlanId);
}
