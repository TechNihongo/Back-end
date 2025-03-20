package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.dto.*;
import org.example.technihongo.entities.*;
import org.example.technihongo.repositories.*;
import org.example.technihongo.services.interfaces.StudentQuizAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class StudentQuizAttemptServiceImpl implements StudentQuizAttemptService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private StudentQuizAttemptRepository studentQuizAttemptRepository;
    @Autowired
    private QuizAnswerResponseRepository quizAnswerResponseRepository;
    @Autowired
    private QuestionAnswerOptionRepository questionAnswerOptionRepository;

    private static final int MAX_ATTEMPTS = 3;
    private static final long WAIT_TIME_MINUTES = 30;

    @Override
    public QuizAttemptResponseDTO attemptQuiz(Integer studentId, QuizAttemptRequestDTO request) {
        Quiz quiz = quizRepository.findByQuizId(request.getQuizId());
        if (quiz == null || quiz.isDeleted()) {
            throw new RuntimeException("Quiz ID not found!");
        }

        int attemptNumber = studentQuizAttemptRepository.findByStudentStudentIdAndQuizQuizId(studentId, request.getQuizId()).size() + 1;
        return processQuizAttempt(studentId, quiz, request, attemptNumber);
    }


    @Override
    public QuizPerformanceReportDTO generatePerformanceReport(Integer studentId, Integer quizId) {
        Quiz quiz = quizRepository.findByQuizId(quizId);
        if (quiz == null || quiz.isDeleted()) {
            throw new RuntimeException("Quiz ID not found!");
        }

        List<StudentQuizAttempt> attempts = studentQuizAttemptRepository.findByStudentStudentIdAndQuizQuizId(studentId, quizId);

        List<AttemptSummaryDTO> summary = attempts.stream().map(attempt -> AttemptSummaryDTO.builder()
                .attemptNumber(attempt.getAttemptNumber())
                .score(attempt.getScore())
                .isPassed(attempt.getIsPassed())
                .dateTaken(attempt.getDateTaken())
                .build()).collect(Collectors.toList());
        BigDecimal averageScore = attempts.isEmpty() ? BigDecimal.ZERO :
                BigDecimal.valueOf(attempts.stream().mapToDouble(a -> a.getScore().doubleValue()).average().orElse(0));
        int passedAttempts = (int) attempts.stream().filter(StudentQuizAttempt::getIsPassed).count();

        return QuizPerformanceReportDTO.builder()
                .quizId(quizId)
                .quizTitle(quiz.getTitle())
                .attempts(summary)
                .averageScore(averageScore)
                .totalAttempts(attempts.size())
                .passedAttempts(passedAttempts)
                .build();
    }

    @Override
    public QuizAttemptResponseDTO retryFailedQuiz(Integer studentId, Integer quizId, QuizAttemptRequestDTO request) {
        Quiz quiz = quizRepository.findByQuizId(quizId);
        if (quiz == null || quiz.isDeleted()) {
            throw new RuntimeException("Quiz ID not found!");
        }

        List<StudentQuizAttempt> attempt = studentQuizAttemptRepository.findByStudentStudentIdAndQuizQuizId(studentId, quizId);
        int attemptNumber = attempt.size() + 1;

        if(attempt.size() >= MAX_ATTEMPTS) {
            boolean hasPassed = attempt.stream().anyMatch(StudentQuizAttempt::getIsPassed);
            if(!hasPassed) {
                StudentQuizAttempt lastAttempt = attempt.stream()
                        .max(Comparator.comparing(StudentQuizAttempt::getDateTaken))
                        .orElseThrow(() -> new RuntimeException("No attempts found!"));

                LocalDateTime lastAttemptTime = lastAttempt.getDateTaken();
                LocalDateTime now = LocalDateTime.now();
                long minutesSinceLastAttempt = lastAttemptTime.until(now, java.time.temporal.ChronoUnit.MINUTES);

                if (minutesSinceLastAttempt  < WAIT_TIME_MINUTES) {
                    long minutesToWait = WAIT_TIME_MINUTES - minutesSinceLastAttempt;
                    throw new RuntimeException("You must wait  " + minutesToWait + " minutes before retrying this quiz.");
                }
            }
        }
        return processQuizAttempt(studentId, quiz, request, attemptNumber);

    }
    private QuizAttemptResponseDTO processQuizAttempt(Integer studentId, Quiz quiz, QuizAttemptRequestDTO request, int attemptNumber) {
        int correctAnswers = 0;
        for (QuizAnswerDTO answer : request.getAnswers()) {
            QuestionAnswerOption option = questionAnswerOptionRepository.findByOptionId(answer.getSelectedOptionId());
            if (option == null) {
                throw new RuntimeException("Option ID not found!");
            }
            if (option.isCorrect()) {
                correctAnswers++;
            }
        }

        BigDecimal score = BigDecimal.valueOf(correctAnswers).divide(BigDecimal.valueOf(quiz.getTotalQuestions()), 2, RoundingMode.HALF_UP);
        boolean isPassed = score.compareTo(quiz.getPassingScore()) >= 0;
        StudentQuizAttempt attempt = StudentQuizAttempt.builder()
                .quiz(quiz)
                .student(Student.builder().studentId(studentId).build())
                .score(score)
                .isPassed(isPassed)
                .timeTaken(0)
                .isCompleted(true)
                .attemptNumber(attemptNumber)
                .dateTaken(LocalDateTime.now())
                .build();
        attempt = studentQuizAttemptRepository.save(attempt);

        for (QuizAnswerDTO answerDTO : request.getAnswers()) {
            QuestionAnswerOption selectedOption = questionAnswerOptionRepository.findByOptionId(answerDTO.getSelectedOptionId());
            if (selectedOption == null) {
                throw new RuntimeException("Option ID not found!");
            }
            QuizAnswerResponse response = QuizAnswerResponse.builder()
                    .studentQuizAttempt(attempt)
                    .selectedOption(selectedOption)
                    .isCorrect(selectedOption.isCorrect())
                    .build();
            quizAnswerResponseRepository.save(response);
        }

        return mapToResponseDTO(attempt);
    }

    private QuizAttemptResponseDTO mapToResponseDTO(StudentQuizAttempt attempt) {
        return QuizAttemptResponseDTO.builder()
                .attemptId(attempt.getAttemptId())
                .quizId(attempt.getQuiz().getQuizId())
                .score(attempt.getScore().multiply(BigDecimal.valueOf(100)))
                .isPassed(attempt.getIsPassed())
                .timeTaken(attempt.getTimeTaken())
                .isCompleted(attempt.getIsCompleted())
                .attemptNumber(attempt.getAttemptNumber())
                .dateTaken(attempt.getDateTaken())
                .build();
    }
}
