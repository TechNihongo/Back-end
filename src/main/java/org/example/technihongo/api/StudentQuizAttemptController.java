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


@RestController
@RequestMapping ("api/student-quiz-attempt")
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
            @RequestBody QuizAttemptRequestDTO request) {
        try {
            String token = authorizationHeader.substring(7);
            Integer userId = jwtUtil.extractUserId(token);
            Integer studentId = studentService.getStudentIdByUserId(userId);

            QuizAttemptResponseDTO response = studentQuizAttemptService.attemptQuiz(studentId, request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Quiz attempted successfully")
                    .data(response)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to attempt quiz: " + e.getMessage())
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
            String token = authorizationHeader.substring(7);
            Integer userId = jwtUtil.extractUserId(token);
            Integer studentId = studentService.getStudentIdByUserId(userId);

            QuizPerformanceReportDTO report = studentQuizAttemptService.generatePerformanceReport(studentId, quizId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Quiz performance report generated successfully")
                    .data(report)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to generate report: " + e.getMessage())
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
            @RequestBody QuizAttemptRequestDTO request) {
        try {
            String token = authorizationHeader.substring(7);
            Integer userId = jwtUtil.extractUserId(token);
            Integer studentId = studentService.getStudentIdByUserId(userId);

            QuizAttemptResponseDTO response = studentQuizAttemptService.retryFailedQuiz(studentId, quizId, request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Quiz retried successfully")
                    .data(response)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retry quiz: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }
}
