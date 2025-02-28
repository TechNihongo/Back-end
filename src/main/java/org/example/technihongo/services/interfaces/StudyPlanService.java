package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.CreateStudyPlanDTO;
import org.example.technihongo.dto.UpdateStudyPlanDTO;
import org.example.technihongo.entities.StudyPlan;
import java.util.List;

public interface StudyPlanService {
    List<StudyPlan> getStudyPlanListByCourseId(Integer courseId);
    List<StudyPlan> getActiveStudyPlanListByCourseId(Integer courseId);
    StudyPlan getStudyPlanById(Integer studyPlanId);
    StudyPlan getActiveStudyPlanById(Integer studyPlanId);
    StudyPlan createStudyPlan(CreateStudyPlanDTO createStudyPlanDTO);
    void updateStudyPlan(Integer studyPlanId, UpdateStudyPlanDTO updateStudyPlanDTO);
    void deleteStudyPlan(Integer studyPlanId);
}
