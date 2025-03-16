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
            @RequestBody SystemFlashcardSetRequestDTO requestDTO) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);

                SystemFlashcardSetResponseDTO response = systemFlashcardSetService.create(userId, requestDTO);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("System Flashcard Set created successfully")
                        .data(response)
                        .build());
            }
            else throw new Exception("Authorization failed!");
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
                            .message("Failed to create System Flashcard Set: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update/{flashcardSetId}")
    public ResponseEntity<ApiResponse> update(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer flashcardSetId,
            @RequestBody SystemFlashcardSetRequestDTO requestDTO) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);

                SystemFlashcardSetResponseDTO response = systemFlashcardSetService.update(userId, flashcardSetId, requestDTO);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("System Flashcard Set updated successfully")
                        .data(response)
                        .build());
            }
            else throw new Exception("Authorization failed!");
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update System Flashcard Set: " + e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/delete/{flashcardSetId}")
    public ResponseEntity<ApiResponse> delete(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer flashcardSetId) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);

                systemFlashcardSetService.deleteSystemFlashcardSet(userId, flashcardSetId);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("System Flashcard Set deleted successfully")
                        .build());
            }
            else throw new Exception("Authorization failed!");
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
                            .message("Failed to delete System Flashcard Set: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/getSysFlashcardSet/{flashcardSetId}")
    public ResponseEntity<ApiResponse> getSystemFlashcardSetById(@PathVariable Integer flashcardSetId) {
        try {
            SystemFlashcardSetResponseDTO response = systemFlashcardSetService.getSystemFlashcardSetById(flashcardSetId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("System Flashcard Set retrieved successfully")
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
                            .message("Failed to retrieve System Flashcard Set: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update-visibility/{setId}")
    public ResponseEntity<ApiResponse> updateVisibility(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer setId,
            @RequestParam Boolean isPublic) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);

                SystemFlashcardSetResponseDTO response = systemFlashcardSetService.updateSystemFlashcardSetVisibility(userId, setId, isPublic);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("System Flashcard Set visibility updated successfully")
                        .data(response)
                        .build());
            }
            else throw new Exception("Authorization failed!");
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
                            .message("Failed to update System Flashcard Set visibility: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("getAllFlashcardOfSet/{setId}")
    public ResponseEntity<ApiResponse> getAllFlashcardsInSet(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer setId) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);

                SystemFlashcardSetResponseDTO response = systemFlashcardSetService.getAllFlashcardsInSet(userId, setId);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Flashcards retrieved successfully")
                        .data(response)
                        .build());
            }
            else throw new UnauthorizedAccessException("Authorization failed!");
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve flashcards: " + e.getMessage())
                            .build());
        }
    }


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllByContentManagerId(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);

                List<SystemFlashcardSetResponseDTO> response = systemFlashcardSetService.systemFlashcardList(userId);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("System Flashcard Sets retrieved successfully")
                        .data(response)
                        .build());
            }
            else throw new Exception("Authorization failed!");
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
                            .message("Failed to retrieve System Flashcard Sets: " + e.getMessage())
                            .build());
        }
    }
}