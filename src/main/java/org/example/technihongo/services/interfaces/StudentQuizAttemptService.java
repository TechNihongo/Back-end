package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.QuizAttemptRequestDTO;
import org.example.technihongo.dto.QuizAttemptResponseDTO;
import org.example.technihongo.dto.QuizPerformanceReportDTO;

public interface StudentQuizAttemptService {
    QuizAttemptResponseDTO attemptQuiz(Integer studentId, QuizAttemptRequestDTO request);

    QuizPerformanceReportDTO generatePerformanceReport(Integer studentId, Integer quizId);

    QuizAttemptResponseDTO retryFailedQuiz(Integer studentId, Integer quizId, QuizAttemptRequestDTO request);

}
