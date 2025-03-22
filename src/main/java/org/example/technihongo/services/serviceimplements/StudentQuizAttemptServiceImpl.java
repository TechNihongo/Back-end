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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
    @Autowired
    private StudentDailyLearningLogRepository dailyLogRepository;
    @Autowired
    private StudentLearningStatisticsRepository statisticsRepository;

    private static final int MAX_ATTEMPTS = 3;
    private static final long WAIT_TIME_MINUTES = 30;
    private static final long MAX_QUIZ_DURATION_MINUTES = 120;

    @Override
    public StartQuizResponseDTO startQuiz(Integer studentId, Integer quizId) {
        validateInput(studentId, quizId);
        Quiz quiz = getValidQuiz(quizId);

        Optional<StudentQuizAttempt> inProgressAttempt = studentQuizAttemptRepository
                .findByStudentStudentIdAndQuizQuizIdAndIsCompletedFalse(studentId, quizId);

        if (inProgressAttempt.isPresent()) {
            StudentQuizAttempt attempt = inProgressAttempt.get();
            if (isAttemptExpired(attempt)) {
                attempt.setIsCompleted(true);
                attempt.setIsPassed(false);
                studentQuizAttemptRepository.save(attempt);
            } else {
                return buildStartQuizResponse(attempt, quiz, true);
            }
        }

        List<StudentQuizAttempt> attempts = studentQuizAttemptRepository.findByStudentStudentIdAndQuizQuizId(studentId, quizId);
        int attemptNumber = attempts.size() + 1;
        validateAttempt(studentId, attempts, attemptNumber);

        StudentQuizAttempt newAttempt = createNewAttempt(studentId, quiz, attemptNumber);
        newAttempt = studentQuizAttemptRepository.save(newAttempt);

        return buildStartQuizResponse(newAttempt, quiz, false);
    }

    @Override
    public QuizAttemptResponseDTO attemptQuiz(Integer studentId, QuizAttemptRequestDTO request) {
        validateInput(studentId, request.getQuizId());
        Quiz quiz = getValidQuiz(request.getQuizId());

        StudentQuizAttempt attempt = studentQuizAttemptRepository.findByAttemptId(request.getAttemptId())
                .orElseThrow(() -> new IllegalArgumentException("No active quiz attempt found. Please start the quiz first."));

        validateAttemptOwnership(studentId, attempt);
        if (attempt.getIsCompleted()) {
            throw new IllegalArgumentException("This quiz attempt has already been completed.");
        }
        if (isAttemptExpired(attempt)) {
            attempt.setIsCompleted(true);
            attempt.setIsPassed(false);
            studentQuizAttemptRepository.save(attempt);
            throw new IllegalArgumentException("This quiz attempt has expired. Please start a new attempt.");
        }

        return processQuizAttempt(attempt, quiz, request);
    }

    @Override
    public QuizAttemptResponseDTO retryFailedQuiz(Integer studentId, Integer quizId, QuizAttemptRequestDTO request) {
        validateInput(studentId, quizId);
        Quiz quiz = getValidQuiz(quizId);

        List<StudentQuizAttempt> attempts = studentQuizAttemptRepository.findByStudentStudentIdAndQuizQuizId(studentId, quizId);
        int attemptNumber = attempts.size() + 1;
        validateAttempt(studentId, attempts, attemptNumber);

        StudentQuizAttempt newAttempt = createNewAttempt(studentId, quiz, attemptNumber);
        newAttempt = studentQuizAttemptRepository.save(newAttempt);

        return processQuizAttempt(newAttempt, quiz, request);
    }

    @Override
    public QuizPerformanceReportDTO generatePerformanceReport(Integer studentId, Integer quizId) {
        validateInput(studentId, quizId);
        Quiz quiz = getValidQuiz(quizId);

        List<StudentQuizAttempt> attempts = studentQuizAttemptRepository.findByStudentStudentIdAndQuizQuizId(studentId, quizId);

        List<AttemptSummaryDTO> summary = attempts.stream().map(attempt -> AttemptSummaryDTO.builder()
                .attemptNumber(attempt.getAttemptNumber())
                .score(attempt.getScore())
                .isPassed(attempt.getIsPassed())
                .dateTaken(attempt.getDateTaken())
                .isCompleted(attempt.getIsCompleted())
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

    private Quiz getValidQuiz(Integer quizId) {
        Quiz quiz = quizRepository.findByQuizId(quizId);
        if (quiz == null || quiz.isDeleted()) {
            throw new IllegalArgumentException("Quiz not found or has been deleted.");
        }
        return quiz;
    }

    private void validateInput(Integer studentId, Integer quizId) {
        if (studentId == null || quizId == null) {
            throw new IllegalArgumentException("Student ID and Quiz ID must not be null.");
        }
    }

    private void validateAttemptOwnership(Integer studentId, StudentQuizAttempt attempt) {
        if (!attempt.getStudent().getStudentId().equals(studentId)) {
            throw new IllegalArgumentException("This quiz attempt does not belong to the current student.");
        }
    }

    private StudentQuizAttempt createNewAttempt(Integer studentId, Quiz quiz, int attemptNumber) {
        return StudentQuizAttempt.builder()
                .quiz(quiz)
                .student(Student.builder().studentId(studentId).build())
                .score(BigDecimal.ZERO)
                .isPassed(false)
                .isCompleted(false)
                .timeTaken(LocalTime.of(0, 0, 0))
                .attemptNumber(attemptNumber)
                .dateTaken(LocalDateTime.now())
                .build();
    }

    // Tách logic xây dựng StartQuizResponseDTO
    private StartQuizResponseDTO buildStartQuizResponse(StudentQuizAttempt attempt, Quiz quiz, boolean resuming) {
        return StartQuizResponseDTO.builder()
                .attemptId(attempt.getAttemptId())
                .quizId(quiz.getQuizId())
                .title(quiz.getTitle())
                .totalQuestions(quiz.getTotalQuestions())
                .attemptNumber(attempt.getAttemptNumber())
                .startTime(attempt.getDateTaken())
                .resuming(resuming)
                .remainingTimeInSeconds(resuming ? calculateRemainingTime(attempt) : MAX_QUIZ_DURATION_MINUTES * 60)
                .build();
    }

    private void validateAttempt(Integer studentId, List<StudentQuizAttempt> attempts, int attemptNumber) {
        if (attemptNumber > MAX_ATTEMPTS) {
            StudentQuizAttempt lastAttempt = attempts.stream()
                    .max(Comparator.comparing(StudentQuizAttempt::getDateTaken))
                    .orElseThrow(() -> new IllegalStateException("No previous attempts found."));
            LocalDateTime lastAttemptTime = lastAttempt.getDateTaken();
            LocalDateTime now = LocalDateTime.now();
            long minutesSinceLastAttempt = lastAttemptTime.until(now, java.time.temporal.ChronoUnit.MINUTES);

            if (minutesSinceLastAttempt < WAIT_TIME_MINUTES) {
                long minutesToWait = WAIT_TIME_MINUTES - minutesSinceLastAttempt;
                throw new IllegalStateException("You have reached the maximum number of attempts (" + MAX_ATTEMPTS + "). Please wait " + minutesToWait + " minutes before retrying.");
            }
        }
    }

    private QuizAttemptResponseDTO processQuizAttempt(StudentQuizAttempt attempt, Quiz quiz, QuizAttemptRequestDTO request) {
        int correctAnswers = 0;
        for (QuizAnswerDTO answer : request.getAnswers()) {
            QuestionAnswerOption option = questionAnswerOptionRepository.findByOptionId(answer.getSelectedOptionId());
            if (option == null) {
                throw new IllegalArgumentException("Option ID " + answer.getSelectedOptionId() + " not found.");
            }
            if (option.isCorrect()) {
                correctAnswers++;
            }
        }

        BigDecimal score = BigDecimal.valueOf(correctAnswers)
                .divide(BigDecimal.valueOf(quiz.getTotalQuestions()), 2, RoundingMode.HALF_UP);
        boolean isPassed = score.compareTo(quiz.getPassingScore()) >= 0;

        LocalTime timeTaken = calculateTimeTaken(attempt);

        attempt.setScore(score);
        attempt.setIsPassed(isPassed);
        attempt.setTimeTaken(timeTaken);
        attempt.setIsCompleted(true);
        attempt = studentQuizAttemptRepository.save(attempt);

        Optional<StudentDailyLearningLog> dailyLogOpt = dailyLogRepository
                .findByStudentStudentIdAndLogDate(attempt.getStudent().getStudentId(), LocalDate.now());
        if (dailyLogOpt.isPresent()) {
            StudentDailyLearningLog dailyLog = dailyLogOpt.get();
            dailyLog.setCompletedQuizzes(dailyLog.getCompletedQuizzes() + 1);
            dailyLogRepository.save(dailyLog);
        }

        Optional<StudentLearningStatistics> statsOpt = statisticsRepository
                .findByStudentStudentId(attempt.getStudent().getStudentId());
        if (statsOpt.isPresent()) {
            StudentLearningStatistics statistics = statsOpt.get();
            statistics.setTotalCompletedQuizzes(statistics.getTotalCompletedQuizzes() + 1);
            statisticsRepository.save(statistics);
        }

        for (QuizAnswerDTO answerDTO : request.getAnswers()) {
            QuestionAnswerOption selectedOption = questionAnswerOptionRepository.findByOptionId(answerDTO.getSelectedOptionId());
            QuizAnswerResponse response = QuizAnswerResponse.builder()
                    .studentQuizAttempt(attempt)
                    .selectedOption(selectedOption)
                    .isCorrect(selectedOption.isCorrect())
                    .build();
            quizAnswerResponseRepository.save(response);
        }

        return mapToResponseDTO(attempt);
    }

    private boolean isAttemptExpired(StudentQuizAttempt attempt) {
        LocalDateTime startTime = attempt.getDateTaken();
        LocalDateTime now = LocalDateTime.now();
        long minutes = Duration.between(startTime, now).toMinutes();
        return minutes > MAX_QUIZ_DURATION_MINUTES;
    }

    private long calculateRemainingTime(StudentQuizAttempt attempt) {
        LocalDateTime startTime = attempt.getDateTaken();
        LocalDateTime now = LocalDateTime.now();
        long elapsedSeconds = Duration.between(startTime, now).getSeconds();
        long maxSeconds = MAX_QUIZ_DURATION_MINUTES * 60;
        return Math.max(0, maxSeconds - elapsedSeconds);
    }

    private LocalTime calculateTimeTaken(StudentQuizAttempt attempt) {
        LocalDateTime startTime = attempt.getDateTaken();
        LocalDateTime endTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime, endTime);
        long seconds = duration.getSeconds();
        return LocalTime.of((int) (seconds / 3600), (int) ((seconds % 3600) / 60), (int) (seconds % 60));
    }

    private QuizAttemptResponseDTO mapToResponseDTO(StudentQuizAttempt attempt) {
        return QuizAttemptResponseDTO.builder()
                .attemptId(attempt.getAttemptId())
                .quizId(attempt.getQuiz().getQuizId())
                .score(attempt.getScore().multiply(BigDecimal.valueOf(100)))
                .isPassed(attempt.getIsPassed())
                .timeTaken(attempt.getTimeTaken().toSecondOfDay())
                .isCompleted(attempt.getIsCompleted())
                .attemptNumber(attempt.getAttemptNumber())
                .dateTaken(attempt.getDateTaken())
                .build();
    }
}