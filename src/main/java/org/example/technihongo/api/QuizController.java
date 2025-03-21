package org.example.technihongo.api;

import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.*;
import org.example.technihongo.entities.Quiz;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.QuizService;
import org.example.technihongo.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@Validated
public class QuizController {
    @Autowired
    private QuizService quizService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private StudentService studentService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllQuizzes(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);

                if (roleId == 1 || roleId == 2) {
                    List<Quiz> quizList = quizService.getQuizList();
                    if (quizList.isEmpty()) {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(false)
                                .message("List quizzes is empty!")
                                .build());
                    } else {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Get All Quizzes: ")
                                .data(quizList)
                                .build());
                    }
                } else {
                    List<Quiz> quizList = quizService.getPublicQuizList();
                    if (quizList.isEmpty()) {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(false)
                                .message("List quizzes is empty!")
                                .build());
                    } else {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Get All Public Quizzes")
                                .data(quizList)
                                .build());
                    }
                }
            }
            else throw new Exception("Authorization failed!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<ApiResponse> viewQuiz(
            @PathVariable Integer quizId,
            @RequestHeader("Authorization") String authorizationHeader){
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);
                int userId = jwtUtil.extractUserId(token);

                if (roleId == 1 || roleId == 2) {
                    Quiz quiz = quizService.getQuizById(quizId);
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Get Quiz")
                            .data(quiz)
                            .build());
                }
                else{
                    Quiz quiz = quizService.getPublicQuizById(userId, quizId);
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Get Quiz")
                            .data(quiz)
                            .build());
                }
            }
            else throw new Exception("Authorization failed!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to get quiz: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createQuiz(
            @RequestBody CreateQuizDTO createQuizDTO,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);

                Quiz quiz = quizService.createQuiz(userId, createQuizDTO);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Quiz created successfully!")
                        .data(quiz)
                        .build());
            }
            else throw new Exception("Authorization failed!");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to create Quiz: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update/{quizId}")
    public ResponseEntity<ApiResponse> updateQuiz(
            @PathVariable Integer quizId,
            @RequestBody UpdateQuizDTO updateQuizDTO) {
        try{
            quizService.updateQuiz(quizId, updateQuizDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Quiz updated successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update Quiz: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update-status/{quizId}")
    public ResponseEntity<ApiResponse> updateQuizStatus(
            @PathVariable Integer quizId,
            @RequestBody UpdateQuizStatusDTO updateQuizStatusDTO) {
        try{
            quizService.updateQuizStatus(quizId, updateQuizStatusDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Quiz updated successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update Quiz: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/creator")
    public ResponseEntity<ApiResponse> getQuizListByCreator(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);

                List<Quiz> quizList = quizService.getListQuizzesByCreatorId(userId);
                if (quizList.isEmpty()) {
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(false)
                            .message("List Quizzes is empty!")
                            .build());
                } else {
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Get Quizzes List By Creator")
                            .data(quizList)
                            .build());
                }
            }
            else throw new Exception("Authorization failed!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to get Quizzes: " + e.getMessage())
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
