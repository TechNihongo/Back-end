package org.example.technihongo.api;

import jakarta.servlet.http.HttpServletRequest;
import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.*;
import org.example.technihongo.entities.StudentQuizAttempt;
import org.example.technihongo.enums.ActivityType;
import org.example.technihongo.enums.ContentType;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.StudentQuizAttemptService;
import org.example.technihongo.services.interfaces.StudentService;
import org.example.technihongo.services.interfaces.UserActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/student-quiz-attempt")
@Validated
public class StudentQuizAttemptController {
    @Autowired
    private StudentQuizAttemptService studentQuizAttemptService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private StudentService studentService;
    @Autowired
    private UserActivityLogService userActivityLogService;

    @PostMapping("/startAttempt/{quizId}")
    public ResponseEntity<ApiResponse> startQuiz(
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest httpRequest,
            @PathVariable Integer quizId) {
        try {
            Integer studentId = extractStudentId(authorizationHeader);
            StartQuizResponseDTO response = studentQuizAttemptService.startQuiz(studentId, quizId);

            String ipAddress = httpRequest.getRemoteAddr();
            String userAgent = httpRequest.getHeader("User-Agent");
            userActivityLogService.trackUserActivityLog(
                    extractUserId(authorizationHeader),
                    ActivityType.START_QUIZ,
                    ContentType.StudentQuizAttempt,
                    response.getAttemptId(),
                    ipAddress,
                    userAgent
            );

            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Quiz started successfully")
                    .data(response)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/attempt")
    public ResponseEntity<ApiResponse> attemptQuiz(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody QuizAttemptRequestDTO request) {
        try {
            Integer studentId = extractStudentId(authorizationHeader);
            QuizAttemptResponseDTO response = studentQuizAttemptService.attemptQuiz(studentId, request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Quiz attempted successfully")
                    .data(response)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/report/{quizId}")
    public ResponseEntity<ApiResponse> generatePerformanceReport(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer quizId) {
        try {
            Integer studentId = extractStudentId(authorizationHeader);
            QuizPerformanceReportDTO report = studentQuizAttemptService.generatePerformanceReport(studentId, quizId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Quiz performance report generated successfully")
                    .data(report)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/retry/{quizId}")
    public ResponseEntity<ApiResponse> retryFailedQuiz(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer quizId,
            @Valid @RequestBody QuizAttemptRequestDTO request) {
        try {
            Integer studentId = extractStudentId(authorizationHeader);
            QuizAttemptResponseDTO response = studentQuizAttemptService.retryFailedQuiz(studentId, quizId, request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Quiz retried successfully")
                    .data(response)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }


    // lấy ra các lần làm quiz (nỗ lực làm bài kiểm tra) của một học sinh
    //Lần làm bài có điểm cao nhất (top attempt)
    //3 lần làm bài gần đây nhất (recent attempts)
    @GetMapping("/top-recent/{quizId}")
    public ResponseEntity<ApiResponse> getTopAndRecentQuizAttempts(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer quizId) {
        try {
            Integer studentId = extractStudentId(authorizationHeader);
            List<StudentQuizAttempt> attemptList = studentQuizAttemptService.getTopAndRecentQuizAttempts(studentId, quizId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Top and recent quiz attempts retrieved successfully")
                    .data(attemptList)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/review/{attemptId}")
    public ResponseEntity<ApiResponse> reviewQuizAttempt(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer attemptId) {
        try {
            Integer studentId = extractStudentId(authorizationHeader);
            ReviewQuizAttemptDTO reviewDTO = studentQuizAttemptService.reviewQuizAttempt(studentId, attemptId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Quiz attempt retrieved successfully")
                    .data(reviewDTO)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    private Integer extractStudentId(String authorizationHeader) throws Exception {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            Integer userId = jwtUtil.extractUserId(token);
            return studentService.getStudentIdByUserId(userId);
        }
        throw new IllegalArgumentException("Authorization header is missing or invalid.");
    }

    private Integer extractUserId(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return jwtUtil.extractUserId(token);
        }
        throw new IllegalArgumentException("Authorization header is missing or invalid.");
    }
}