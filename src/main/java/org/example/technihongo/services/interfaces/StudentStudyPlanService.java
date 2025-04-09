package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.*;

import java.util.List;

public interface StudentStudyPlanService {

    StudentStudyPlanDTO enrollStudentInStudyPlan(EnrollStudyPlanRequest request);

    StudentStudyPlanDTO switchStudyPlan(SwitchStudyPlanRequestDTO request);

    List<StudyPlanDTO> getAvailableStudyPlans(Integer studentId);

    StudentStudyPlanDTO getActiveStudyPlan(Integer studentId, Integer courseId);

    List<StudentStudyPlanDTO> getStudyPlanHistory(Integer studentId);
}
