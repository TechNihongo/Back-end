package org.example.technihongo.api;

import org.example.technihongo.dto.QuestionAnswerOptionListDTO;
import org.example.technihongo.dto.QuestionWithOptionsDTO;
import org.example.technihongo.dto.QuestionWithOptionsRespondDTO;
import org.example.technihongo.entities.Question;
import org.example.technihongo.entities.QuestionAnswerOption;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.QuestionAnswerOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/option")
@Validated
public class QuestionAnswerOptionController {
    @Autowired
    private QuestionAnswerOptionService questionAnswerOptionService;

    @GetMapping("/question/{questionId}")
    public ResponseEntity<ApiResponse> getOptionsByQuestionId(@PathVariable Integer questionId) throws Exception {
        try{
            List<QuestionAnswerOption> optionList = questionAnswerOptionService.getOptionListByQuestionId(questionId);
            if(optionList.isEmpty()){
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("List Options is empty!")
                        .build());
            }else{
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get All Options By Question")
                        .data(optionList)
                        .build());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Get QuestionAnswerOptions failed: " + e.getMessage())
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
    public ResponseEntity<ApiResponse> viewQuestionAnswerOption(@PathVariable Integer id) throws Exception {
        try{
            QuestionAnswerOption option = questionAnswerOptionService.getOptionById(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Get QuestionAnswerOption")
                    .data(option)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Get QuestionAnswerOption failed: " + e.getMessage())
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
    public ResponseEntity<ApiResponse> createQuestionAnswerOptions(@RequestBody QuestionAnswerOptionListDTO questionAnswerOptionListDTO){
        try {
            QuestionWithOptionsRespondDTO question = questionAnswerOptionService.createAnswerOptionList(questionAnswerOptionListDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("QuestionAnswerOptions created successfully!")
                    .data(question)
                    .build());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to create QuestionAnswerOptions: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse> updateQuestionAnswerOptions(@RequestBody QuestionAnswerOptionListDTO questionAnswerOptionListDTO) {
        try{
            QuestionWithOptionsRespondDTO question =  questionAnswerOptionService.updateAnswerOptionList(questionAnswerOptionListDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("QuestionAnswerOptions updated successfully")
                    .data(question)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update QuestionAnswerOptions: " + e.getMessage())
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
