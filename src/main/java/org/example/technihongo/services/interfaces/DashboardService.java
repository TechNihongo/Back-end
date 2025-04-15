package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.AdminOverviewDTO;
import org.example.technihongo.dto.LearningStatsDTO;
import org.example.technihongo.dto.QuizStatsDTO;
import org.example.technihongo.dto.StudentSpendingDTO;

import java.util.List;

public interface DashboardService {
    StudentSpendingDTO getStudentSpending(Integer studentId);
    AdminOverviewDTO getAdminOverview();
    LearningStatsDTO getLearningStats(Integer studentId);
    List<QuizStatsDTO> getWeeklyQuizStats(Integer studentId);
}
