package org.example.technihongo.services.interfaces;

import org.example.technihongo.entities.StudentResourceProgress;

import java.util.List;

public interface StudentResourceProgressService {
    void trackLearningResourceProgress(Integer studentId, Integer resourceId, String notes);
    List<StudentResourceProgress> getAllStudentResourceProgress(Integer studentId);
    StudentResourceProgress viewStudentResourceProgress(Integer studentId, Integer resourceId);
    StudentResourceProgress writeNoteForLearningResource(Integer studentId, Integer resourceId, String notes);
}
