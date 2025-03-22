package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.QuizAttemptRequestDTO;
import org.example.technihongo.dto.QuizAttemptResponseDTO;
import org.example.technihongo.dto.QuizPerformanceReportDTO;
import org.example.technihongo.dto.StartQuizResponseDTO;

public interface StudentQuizAttemptService {

    StartQuizResponseDTO startQuiz(Integer studentId, Integer quizId);

    QuizAttemptResponseDTO attemptQuiz(Integer studentId, QuizAttemptRequestDTO request);

    QuizPerformanceReportDTO generatePerformanceReport(Integer studentId, Integer quizId);

    QuizAttemptResponseDTO retryFailedQuiz(Integer studentId, Integer quizId, QuizAttemptRequestDTO request);

}
