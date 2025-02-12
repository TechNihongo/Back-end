package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.CourseWithStudyPlanListDTO;
import org.example.technihongo.dto.CreateCourseStudyPlanDTO;
import org.example.technihongo.entities.Course;
import org.example.technihongo.entities.CourseStudyPlan;
import org.example.technihongo.entities.StudyPlan;

import java.util.List;
import java.util.Optional;

public interface CourseStudyPlanService {
    List<CourseWithStudyPlanListDTO> getCourseListWithStudyPlans();
    Optional<CourseWithStudyPlanListDTO> getCourseWithStudyPlans(Integer courseId);
    CourseStudyPlan createCourseStudyPlan(CreateCourseStudyPlanDTO createCourseStudyPlanDTO);
}
