package org.example.technihongo.api;

import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.FlashcardSetProgressDTO;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.StudentFlashcardSetProgressService;
import org.example.technihongo.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flashcard-set-progress")
public class StudentFlashcardSetProgressController {
    @Autowired
    private StudentFlashcardSetProgressService setProgressService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private StudentService studentService;

    @GetMapping("/all/{studentId}")
    public ResponseEntity<ApiResponse> getAllStudentAndSystemSetProgress(
            @PathVariable Integer studentId) {
        try {
            List<FlashcardSetProgressDTO> progressList = setProgressService.getAllStudentAndSystemSetProgress(studentId);

            if (progressList.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("No flashcard set progress found for this student")
                        .build());
            } else {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Flashcard set progress retrieved successfully")
                        .data(progressList)
                        .build());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve flashcard set progress: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/set/{studentId}")
    public ResponseEntity<ApiResponse> getStudentOrSystemSetProgress(
            @PathVariable Integer studentId,
            @RequestParam Integer setId,
            @RequestParam(defaultValue = "false") boolean isSystemSet) {
        try {
            FlashcardSetProgressDTO progress = setProgressService.getStudentOrSystemSetProgress(studentId, setId, isSystemSet);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Flashcard set progress retrieved successfully")
                    .data(progress)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve flashcard set progress: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/track")
    public ResponseEntity<ApiResponse> trackFlashcardSetProgress(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam Integer setId,
            @RequestParam(defaultValue = "true") boolean isSystemSet,
            @RequestParam(defaultValue = "") Integer currentFlashcardId) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);

                setProgressService.trackFlashcardSetProgress(studentId, setId, isSystemSet, currentFlashcardId);

                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Flashcard set progress tracked successfully")
                        .data(null)
                        .build());
            }  else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Unauthorized")
                                .build());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to track flashcard set progress: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/complete")
    public ResponseEntity<ApiResponse> completeFlashcardSetProgress(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam Integer setId,
            @RequestParam(defaultValue = "true") boolean isSystemSet) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);

                setProgressService.completeFlashcardSetProgress(studentId, setId, isSystemSet, null);

                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Flashcard set progress completed successfully")
                        .data(null)
                        .build());
            }  else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Unauthorized")
                                .build());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to complete flashcard set progress: " + e.getMessage())
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
