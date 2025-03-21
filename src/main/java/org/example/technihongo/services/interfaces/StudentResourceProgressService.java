package org.example.technihongo.services.interfaces;

public interface StudentResourceProgressService {
    void trackLearningResourceProgress(Integer studentId, Integer resourceId, String notes);
}
