package org.example.technihongo.api;

import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.SystemFlashcardSetRequestDTO;
import org.example.technihongo.dto.SystemFlashcardSetResponseDTO;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.exception.UnauthorizedAccessException;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.SystemFlashcardSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/system-flashcard-set")
public class SystemFlashcardSetController {
    @Autowired
    private SystemFlashcardSetService systemFlashcardSetService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> create(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody SystemFlashcardSetRequestDTO requestDTO) {
        try {
            Integer userId = extractUserId(authorizationHeader);
            SystemFlashcardSetResponseDTO response = systemFlashcardSetService.create(userId, requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.builder()
                            .success(true)
                            .message("System Flashcard Set created successfully")
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

    @PatchMapping("/update/{flashcardSetId}")
    public ResponseEntity<ApiResponse> update(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer flashcardSetId,
            @Valid @RequestBody SystemFlashcardSetRequestDTO requestDTO) {
        try {
            Integer userId = extractUserId(authorizationHeader);
            SystemFlashcardSetResponseDTO response = systemFlashcardSetService.update(userId, flashcardSetId, requestDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("System Flashcard Set updated successfully")
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

    @DeleteMapping("/delete/{flashcardSetId}")
    public ResponseEntity<ApiResponse> delete(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer flashcardSetId) {
        try {
            Integer userId = extractUserId(authorizationHeader);
            systemFlashcardSetService.deleteSystemFlashcardSet(userId, flashcardSetId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("System Flashcard Set marked as deleted successfully")
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

    @GetMapping("/getSysFlashcardSet/{flashcardSetId}")
    public ResponseEntity<ApiResponse> getSystemFlashcardSetById(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer flashcardSetId) {
        try {
            Integer userId = extractUserId(authorizationHeader);
            // Sử dụng getAllFlashcardsInSet để lấy đầy đủ thông tin và kiểm tra quyền truy cập
            SystemFlashcardSetResponseDTO response = systemFlashcardSetService.getAllFlashcardsInSet(userId, flashcardSetId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("System Flashcard Set retrieved successfully")
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

    @PatchMapping("/update-visibility/{setId}")
    public ResponseEntity<ApiResponse> updateVisibility(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer setId,
            @RequestParam(required = true) Boolean isPublic) {
        try {
            Integer userId = extractUserId(authorizationHeader);
            SystemFlashcardSetResponseDTO response = systemFlashcardSetService.updateSystemFlashcardSetVisibility(userId, setId, isPublic);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("System Flashcard Set visibility updated successfully")
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
            @PathVariable Integer setId) {
        try {
            Integer userId = extractUserId(authorizationHeader);
            SystemFlashcardSetResponseDTO response = systemFlashcardSetService.getAllFlashcardsInSet(userId, setId);
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
    public ResponseEntity<ApiResponse> getAllByContentManagerId(
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            Integer userId = extractUserId(authorizationHeader);
            List<SystemFlashcardSetResponseDTO> response = systemFlashcardSetService.systemFlashcardList(userId);
            if (response.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("No active System Flashcard Sets found for this content manager")
                        .data(Collections.emptyList())
                        .build());
            }
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("System Flashcard Sets retrieved successfully")
                    .data(response)
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

    private Integer extractUserId(String authorizationHeader) throws Exception {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return jwtUtil.extractUserId(token);
        }
        throw new UnauthorizedAccessException("Authorization failed! Invalid or missing token.");
    }
}