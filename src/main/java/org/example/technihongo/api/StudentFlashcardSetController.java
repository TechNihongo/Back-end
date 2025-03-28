package org.example.technihongo.api;

import jakarta.validation.Valid;
import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.CreateFlashcardSetFromResourceDTO;
import org.example.technihongo.dto.FlashcardSetRequestDTO;
import org.example.technihongo.dto.FlashcardSetResponseDTO;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.exception.UnauthorizedAccessException;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.StudentFlashcardSetProgressService;
import org.example.technihongo.services.interfaces.StudentFlashcardSetService;
import org.example.technihongo.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/student-flashcard-set")
public class StudentFlashcardSetController {
    @Autowired
    private StudentFlashcardSetService studentFlashcardSetService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentFlashcardSetProgressService studentFlashcardSetProgressService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createFlashcardSet(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody FlashcardSetRequestDTO request) {
        try {
            Integer studentId = extractStudentId(authorizationHeader);
            FlashcardSetResponseDTO response = studentFlashcardSetService.createFlashcardSet(studentId, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.builder()
                            .success(true)
                            .message("Flashcard set created successfully")
                            .data(response)
                            .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
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

    @PatchMapping("/{setId}/update")
    public ResponseEntity<ApiResponse> updateFlashcardSet(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("setId") Integer flashcardSetId,
            @Valid @RequestBody FlashcardSetRequestDTO request) {
        try {
            Integer studentId = extractStudentId(authorizationHeader);
            FlashcardSetResponseDTO response = studentFlashcardSetService.updateFlashcardSet(studentId, flashcardSetId, request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Flashcard set updated successfully")
                    .data(response)
                    .build());
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
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

    @DeleteMapping("/delete/{setId}")
    public ResponseEntity<ApiResponse> deleteFlashcardSet(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("setId") Integer flashcardSetId) {
        try {
            Integer studentId = extractStudentId(authorizationHeader);
            studentFlashcardSetService.deleteFlashcardSet(studentId, flashcardSetId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Flashcard set marked as deleted successfully")
                    .build());
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
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

    @GetMapping("/getUserFlashcard/{setId}")
    public ResponseEntity<ApiResponse> getFlashcardSet(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("setId") Integer flashcardSetId) {
        try {
            Integer studentId = extractStudentId(authorizationHeader);

            FlashcardSetResponseDTO response = studentFlashcardSetService.getAllFlashcardsInSet(studentId, flashcardSetId);

            if(studentId != null){
                studentFlashcardSetProgressService.trackFlashcardSetProgress(studentId, flashcardSetId, false, null);
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Flashcard set retrieved successfully")
                    .data(response)
                    .build());
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
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

    @PatchMapping("/updateVisibility/{setId}")
    public ResponseEntity<ApiResponse> updateFlashcardSetVisibility(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("setId") Integer flashcardSetId,
            @RequestParam(required = true) Boolean isPublic) {
        try {
            Integer studentId = extractStudentId(authorizationHeader);
            FlashcardSetResponseDTO response = studentFlashcardSetService.updateFlashcardSetVisibility(studentId, flashcardSetId, isPublic);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Flashcard set visibility updated successfully")
                    .data(response)
                    .build());
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
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

    @GetMapping("/getAllFlashcardOfSet/{setId}")
    public ResponseEntity<ApiResponse> getAllFlashcardsInSet(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("setId") Integer flashcardSetId) {
        try {
            Integer studentId = extractStudentId(authorizationHeader);
            FlashcardSetResponseDTO response = studentFlashcardSetService.getAllFlashcardsInSet(studentId, flashcardSetId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Flashcards retrieved successfully")
                    .data(response)
                    .build());
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
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

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllStudentFlashcardSet(
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            Integer studentId = extractStudentId(authorizationHeader);
            List<FlashcardSetResponseDTO> studentFlashcardList = studentFlashcardSetService.studentFlashcardList(studentId);
            if (studentFlashcardList.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Student has no active flashcard sets")
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

    @GetMapping("/searchTitle")
    public ResponseEntity<ApiResponse> searchTitle(@RequestParam String keyword) {
        try {
            List<FlashcardSetResponseDTO> responseDTO = studentFlashcardSetService.searchTitle(keyword);
            if (responseDTO.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("No active flashcard sets found for keyword: " + keyword)
                        .data(Collections.emptyList())
                        .build());
            }
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Found flashcard sets for keyword: " + keyword)
                    .data(responseDTO)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to search flashcard sets: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/from-resource")
    public ResponseEntity<ApiResponse> createFlashcardSetFromResource(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody CreateFlashcardSetFromResourceDTO request) {
        try {
            Integer studentId = extractStudentId(authorizationHeader);
            FlashcardSetResponseDTO responseDTO = studentFlashcardSetService.createFlashcardSetFromResource(studentId, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.builder()
                            .success(true)
                            .message("Flashcard set created successfully from learning resource")
                            .data(responseDTO)
                            .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
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
        throw new Exception("Authorization failed!");
    }
}