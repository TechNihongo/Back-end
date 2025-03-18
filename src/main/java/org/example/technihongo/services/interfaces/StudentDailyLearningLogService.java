package org.example.technihongo.services.interfaces;

import org.example.technihongo.entities.StudentDailyLearningLog;

public interface StudentDailyLearningLogService {
    void trackStudentDailyLearningLog(Integer studentId, Integer studyTimeInput);
    StudentDailyLearningLog getStudentDailyLearningLog(Integer studentId);
}
