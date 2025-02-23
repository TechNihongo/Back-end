package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CreateQuizQuestionDTO;
import org.example.technihongo.dto.UpdateQuizQuestionOrderDTO;
import org.example.technihongo.entities.QuizQuestion;
import org.example.technihongo.entities.Quiz;
import org.example.technihongo.entities.QuizQuestion;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.QuizQuestionService;
import org.example.technihongo.services.interfaces.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-question")
@RequiredArgsConstructor
public class QuizQuestionController {
    @Autowired
    private QuizQuestionService quizQuestionService;
    @Autowired
    private QuizService quizService;

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<ApiResponse> getQuizQuestionListByQuizId(@PathVariable Integer quizId) throws Exception {
        try{
            List<QuizQuestion> quizQuestions = quizQuestionService.getQuizQuestionsByQuizId(quizId);
            if(quizQuestions.isEmpty()){
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("List QuizQuestion is empty!")
                        .build());
            }else{
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get QuizQuestion List")
                        .data(quizQuestions)
                        .build());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to get QuizQuestions: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getQuizQuestionById(@PathVariable Integer id) throws Exception {
        try{
            QuizQuestion quizQuestion = quizQuestionService.getQuizQuestionById(id);
            if(quizQuestion == null){
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("QuizQuestion not found!")
                        .build());
            }else{
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get QuizQuestion")
                        .data(quizQuestion)
                        .build());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to get QuizQuestion: " + e.getMessage())
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
    public ResponseEntity<ApiResponse> createQuizQuestion(@RequestBody CreateQuizQuestionDTO createQuizQuestionDTO){
        try {
            QuizQuestion quizQuestion = quizQuestionService.createQuizQuestion(createQuizQuestionDTO);
            quizService.updateTotalQuestions(createQuizQuestionDTO.getQuizId());
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("QuizQuestion created successfully!")
                    .data(quizQuestion)
                    .build());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to create QuizQuestion: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update-order/{quizId}")
    public ResponseEntity<ApiResponse> updateQuizQuestionOrder(@PathVariable Integer quizId,
                                                             @RequestBody UpdateQuizQuestionOrderDTO updateQuizQuestionOrderDTO) {
        try{
            quizQuestionService.updateQuizQuestionOrder(quizId, updateQuizQuestionOrderDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("QuizQuestion updated successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update QuizQuestion: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteQuizQuestion(@PathVariable Integer id) {
        try{
            Integer quizId = quizQuestionService.getQuizQuestionById(id).getQuiz().getQuizId();
            quizQuestionService.deleteQuizQuestion(id);
            quizService.updateTotalQuestions(quizId);

            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("QuizQuestion removed successfully!")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to delete QuizQuestion: " + e.getMessage())
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
