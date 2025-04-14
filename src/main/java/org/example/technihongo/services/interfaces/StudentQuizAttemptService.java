package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.*;
import org.example.technihongo.entities.StudentQuizAttempt;

import java.util.List;

public interface StudentQuizAttemptService {

    StartQuizResponseDTO startQuiz(Integer studentId, Integer quizId);

    QuizAttemptResponseDTO attemptQuiz(Integer studentId, QuizAttemptRequestDTO request);

    QuizPerformanceReportDTO generatePerformanceReport(Integer studentId, Integer quizId);

    QuizAttemptResponseDTO retryFailedQuiz(Integer studentId, Integer quizId, QuizAttemptRequestDTO request);

    List<StudentQuizAttempt> getTopAndRecentQuizAttempts(Integer studentId, Integer quizId);
    ReviewQuizAttemptDTO reviewQuizAttempt(Integer studentId, Integer attemptId);
    AttemptStatusDTO getAttemptStatus(Integer studentId, Integer quizId);
}
