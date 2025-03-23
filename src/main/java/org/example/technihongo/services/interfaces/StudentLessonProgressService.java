package org.example.technihongo.services.interfaces;

import org.example.technihongo.entities.StudentLessonProgress;

import java.util.List;

public interface StudentLessonProgressService {
    void trackStudentLessonProgress(Integer studentId, Integer lessonId);
    List<StudentLessonProgress> viewAllStudentLessonProgressInStudyPlan(Integer studentId, Integer studyPlanId);
}
