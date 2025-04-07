package org.example.technihongo.api;

import jakarta.servlet.http.HttpServletRequest;
import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.FlashcardRequestDTO;
import org.example.technihongo.dto.FlashcardResponseDTO;
import org.example.technihongo.dto.PageResponseDTO;
import org.example.technihongo.enums.ActivityType;
import org.example.technihongo.enums.ContentType;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.FlashcardProgressService;
import org.example.technihongo.services.interfaces.FlashcardService;
import org.example.technihongo.services.interfaces.StudentService;
import org.example.technihongo.services.interfaces.UserActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flashcard")
public class FlashcardController {
    @Autowired
    private FlashcardService flashcardService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private FlashcardProgressService flashcardProgressService;
    @Autowired
    private UserActivityLogService userActivityLogService;


    @PostMapping("/{setId}/studentCreate")
    public ResponseEntity<ApiResponse> createStudentFlashcards(
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest httpRequest,
            @PathVariable("setId") Integer flashcardSetId,
            @RequestBody List<FlashcardRequestDTO> requests) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);

                String ipAddress = httpRequest.getRemoteAddr();
                String userAgent = httpRequest.getHeader("User-Agent");
                userActivityLogService.trackUserActivityLog(
                        userId,
                        ActivityType.CREATE,
                        ContentType.Flashcard,
                        null,
                        ipAddress,
                        userAgent
                );

                List<FlashcardResponseDTO> responseDTO = flashcardService.createStudentFlashcards(studentId, flashcardSetId, requests);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Flashcards created successfully")
                        .data(responseDTO)
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
                            .message("Failed to create flashcards: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/{setId}/systemCreate")
    public ResponseEntity<ApiResponse> createSystemFlashcards(
            @PathVariable("setId") Integer flashcardSetId,
            @RequestBody List<FlashcardRequestDTO> requests,
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest httpRequest) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);

                String ipAddress = httpRequest.getRemoteAddr();
                String userAgent = httpRequest.getHeader("User-Agent");
                userActivityLogService.trackUserActivityLog(
                        userId,
                        ActivityType.CREATE,
                        ContentType.Flashcard,
                        null,
                        ipAddress,
                        userAgent
                );

                List<FlashcardResponseDTO> responses = flashcardService.createSystemFlashcards(userId, flashcardSetId, requests);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Flashcards created successfully")
                        .data(responses)
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
                            .message("Failed to create flashcards: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }
    @PatchMapping("/{flashcardId}/update")
    public ResponseEntity<ApiResponse> updateFlashcard(
            @PathVariable Integer flashcardId,
            @RequestBody FlashcardRequestDTO request,
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest httpRequest) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);

                String ipAddress = httpRequest.getRemoteAddr();
                String userAgent = httpRequest.getHeader("User-Agent");
                userActivityLogService.trackUserActivityLog(
                        userId,
                        ActivityType.UPDATE,
                        ContentType.Flashcard,
                        flashcardId,
                        ipAddress,
                        userAgent
                );

                FlashcardResponseDTO response = flashcardService.updateFlashcard(userId, studentId, flashcardId, request);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Flashcard updated successfully")
                        .data(response)
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
                            .message("Failed to update flashcard: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }



    @DeleteMapping("/delete/{flashcardId}")
    public ResponseEntity<ApiResponse> deleteFlashcard(
            @PathVariable Integer flashcardId,
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest httpRequest) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);

                String ipAddress = httpRequest.getRemoteAddr();
                String userAgent = httpRequest.getHeader("User-Agent");
                userActivityLogService.trackUserActivityLog(
                        userId,
                        ActivityType.DELETE,
                        ContentType.Flashcard,
                        flashcardId,
                        ipAddress,
                        userAgent
                );

                flashcardService.deleteFlashcard(userId, flashcardId);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Flashcard deleted successfully")
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
                            .message("Failed to delete flashcard: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }
    @GetMapping("/getFlashcard/{flashcardId}")
    public ResponseEntity<ApiResponse> getFlashcard(
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest httpRequest,
            @PathVariable Integer flashcardId) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);
                String ipAddress = httpRequest.getRemoteAddr();
                String userAgent = httpRequest.getHeader("User-Agent");
                userActivityLogService.trackUserActivityLog(
                        studentId,
                        ActivityType.VIEW,
                        ContentType.Flashcard,
                        null,
                        ipAddress,
                        userAgent
                );
                FlashcardResponseDTO responseDTO = flashcardService.getFlashcardById(flashcardId);

                if(studentId != null){
                    flashcardProgressService.updateFlashcardProgress(studentId, flashcardId, null);
                }

                return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Flashcard retrieved successfully")
                                .data(responseDTO)
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
                            .message("Failed to retrieve flashcard: " + e.getMessage())
                            .build());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/studentFlashcardSet/{setId}")
    public ResponseEntity<ApiResponse> getStudentFlashcards(
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest httpRequest,
            @PathVariable Integer setId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "cardOrder") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);
                String ipAddress = httpRequest.getRemoteAddr();
                String userAgent = httpRequest.getHeader("User-Agent");
                userActivityLogService.trackUserActivityLog(
                        studentId,
                        ActivityType.VIEW,
                        ContentType.Flashcard,
                        null,
                        ipAddress,
                        userAgent
                );

                if (studentId == null) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(ApiResponse.builder()
                                    .success(false)
                                    .message("No student profile found for this user")
                                    .build());
                }

                PageResponseDTO<FlashcardResponseDTO> responseDTO = flashcardService.getStudentFlashcards(
                        studentId, setId, pageNo, pageSize, sortBy, sortDir);

                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Student flashcards retrieved successfully")
                        .data(responseDTO)
                        .build());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Unauthorized")
                                .build());
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Flashcard set not found: " + e.getMessage())
                            .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Access denied: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/systemFlashcardSet/{setId}")
    public ResponseEntity<ApiResponse> getSystemFlashcards(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer setId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "cardOrder") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);

                PageResponseDTO<FlashcardResponseDTO> responseDTO = flashcardService.getSystemFlashcards(
                        userId, setId, pageNo, pageSize, sortBy, sortDir);

                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("System flashcards retrieved successfully")
                        .data(responseDTO)
                        .build());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Unauthorized")
                                .build());
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Flashcard set not found: " + e.getMessage())
                            .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Access denied: " + e.getMessage())
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
