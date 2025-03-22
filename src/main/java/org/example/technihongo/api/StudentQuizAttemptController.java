package org.example.technihongo.api;

import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.QuizAttemptRequestDTO;
import org.example.technihongo.dto.QuizAttemptResponseDTO;
import org.example.technihongo.dto.QuizPerformanceReportDTO;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.StudentQuizAttemptService;
import org.example.technihongo.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/student-quiz-attempt")
@Validated
public class StudentQuizAttemptController {
    @Autowired
    private StudentQuizAttemptService studentQuizAttemptService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private StudentService studentService;

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

    // Helper method to extract studentId from JWT
    private Integer extractStudentId(String authorizationHeader) throws Exception {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            Integer userId = jwtUtil.extractUserId(token);
            return studentService.getStudentIdByUserId(userId);
        }
        throw new IllegalArgumentException("Authorization header is missing or invalid.");
    }
}