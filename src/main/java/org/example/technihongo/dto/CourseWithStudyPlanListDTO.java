package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.technihongo.entities.Course;
import org.example.technihongo.entities.StudyPlan;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseWithStudyPlanListDTO {
    private Course course;
    private List<StudyPlan> studyPlanList;
}
