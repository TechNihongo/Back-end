package org.example.technihongo.api;

import org.example.technihongo.dto.*;
import org.example.technihongo.entities.LessonResource;
import org.example.technihongo.entities.Question;
import org.example.technihongo.entities.StudyPlan;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/question")
@Validated
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllQuestions() throws Exception {
        try{
            List<Question> questionList = questionService.getQuestionList();
            if(questionList.isEmpty()){
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("List Questions is empty!")
                        .build());
            }else{
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get All Questions")
                        .data(questionList)
                        .build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> viewQuestion(@PathVariable Integer id) throws Exception {
        try{
            Question question = questionService.getQuestionById(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Get Question")
                    .data(question)
                    .build());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Get Question failed: " + e.getMessage())
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
    public ResponseEntity<ApiResponse> createQuestion(@RequestBody CreateUpdateQuestionDTO createUpdateQuestionDTO){
        try {
            Question question = questionService.createQuestion(createUpdateQuestionDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Question created successfully!")
                    .data(question)
                    .build());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to create question: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateQuestion(@PathVariable Integer id,
                                                            @RequestBody CreateUpdateQuestionDTO createUpdateQuestionDTO) {
        try{
            questionService.updateQuestion(id, createUpdateQuestionDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Question updated successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update question: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/options/create")
    public ResponseEntity<ApiResponse> createQuestionWithOptions(@RequestBody QuestionWithOptionsDTO questionWithOptionsDTO){
        try {
            QuestionWithOptionsRespondDTO question = questionService.createQuestionWithOptions(questionWithOptionsDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Question created successfully!")
                    .data(question)
                    .build());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to create question: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/options/update/{id}")
    public ResponseEntity<ApiResponse> updateQuestionWithOptions(@PathVariable Integer id,
                                                      @RequestBody QuestionWithOptionsDTO questionWithOptionsDTO) {
        try{
            QuestionWithOptionsRespondDTO question =  questionService.updateQuestionWithOptions(id, questionWithOptionsDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Question updated successfully")
                    .data(question)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update question: " + e.getMessage())
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
