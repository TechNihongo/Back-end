package org.example.technihongo.api;

import org.example.technihongo.dto.FlashcardSetRequestDTO;
import org.example.technihongo.dto.FlashcardSetResponseDTO;
import org.example.technihongo.entities.Student;
import org.example.technihongo.entities.StudentFlashcardSet;
import org.example.technihongo.entities.StudyPlan;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.exception.UnauthorizedAccessException;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.StudentFlashcardSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/flashcard-set")
public class StudentFlashcardSetController {
    @Autowired
    private StudentFlashcardSetService studentFlashcardSetService;

    @PostMapping("/{studentId}/create")
    public ResponseEntity<ApiResponse> createFlashcardSet(
            @PathVariable Integer studentId,
            @RequestBody FlashcardSetRequestDTO request) {
        try {
            FlashcardSetResponseDTO response = studentFlashcardSetService.createFlashcardSet(studentId, request);
            return ResponseEntity.ok(ApiResponse.<FlashcardSetResponseDTO>builder()
                    .success(true)
                    .message("Flashcard set created successfully")
                    .data(response)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<FlashcardSetResponseDTO>builder()
                            .success(false)
                            .message("Failed to create flashcard set: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<FlashcardSetResponseDTO>builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/{studentId}/{setId}/update")
    public ResponseEntity<ApiResponse> updateFlashcardSet(
            @PathVariable Integer studentId,
            @PathVariable("setId") Integer flashcardSetId,
            @RequestBody FlashcardSetRequestDTO request) {
        try {
            FlashcardSetResponseDTO response = studentFlashcardSetService.updateFlashcardSet(studentId, flashcardSetId, request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Flashcard set updated successfully")
                    .data(response)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update flashcard set: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/delete/{studentId}/{setId}")
    public ResponseEntity<ApiResponse> deleteFlashcardSet(
            @PathVariable Integer studentId,
            @PathVariable("setId") Integer flashcardSetId) {
        try {
            studentFlashcardSetService.deleteFlashcardSet(studentId, flashcardSetId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Flashcard set deleted successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to delete flashcard set: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/getUserFlashcard/{setId}")
    public ResponseEntity<ApiResponse> getFlashcardSet(
            @PathVariable("setId") Integer flashcardSetId) {
        try {
            FlashcardSetResponseDTO response = studentFlashcardSetService.getFlashcardSetById(flashcardSetId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Flashcard set retrieved successfully")
                    .data(response)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve flashcard set: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/updateVisibility/{studentId}/{setId}")
    public ResponseEntity<ApiResponse> updateFlashcardSetVisibility(
            @PathVariable Integer studentId,
            @PathVariable("setId") Integer flashcardSetId,
            @RequestParam(required = false) Boolean isPublic) {
        if (isPublic == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Parameter 'isPublic' is required")
                            .build());
        }

        try {
            FlashcardSetResponseDTO response = studentFlashcardSetService.updateFlashcardSetVisibility(studentId, flashcardSetId, isPublic);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Flashcard set visibility updated successfully")
                    .data(response)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update flashcard set visibility: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }
    @GetMapping("getAllFlashcardOfSet/{userId}/{setId}")
    public ResponseEntity<ApiResponse> getAllFlashcardsInSet(
            @PathVariable("userId") Integer userId,
            @PathVariable("setId") Integer flashcardSetId) {
        try {
            FlashcardSetResponseDTO response = studentFlashcardSetService.getAllFlashcardsInSet(userId, flashcardSetId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Flashcards retrieved successfully")
                    .data(response)
                    .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve flashcards: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }


    @GetMapping("/all/{studentId}")
    public ResponseEntity<ApiResponse> getAllStudentFlashcardSet(@PathVariable Integer studentId) {
        try {
            List<FlashcardSetResponseDTO> studentFlashcardList = studentFlashcardSetService.studentFlashcardList(studentId);

            if (studentFlashcardList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(ApiResponse.builder()
                                .success(true)
                                .message("Student has no flashcard sets")
                                .data(Collections.emptyList())
                                .build());
            }
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Retrieved all flashcard sets successfully")
                    .data(studentFlashcardList)
                    .build());

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Student not found with ID: " + studentId)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Error retrieving flashcard sets: " + e.getMessage())
                            .build());
        }
    }




}
