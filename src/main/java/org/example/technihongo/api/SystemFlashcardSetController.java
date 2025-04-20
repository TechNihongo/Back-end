package org.example.technihongo.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.SystemFlashcardSetRequestDTO;
import org.example.technihongo.dto.SystemFlashcardSetResponseDTO;
import org.example.technihongo.dto.UpdateFlashcardOrderDTO;
import org.example.technihongo.enums.ActivityType;
import org.example.technihongo.enums.ContentType;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.exception.UnauthorizedAccessException;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.StudentFlashcardSetProgressService;
import org.example.technihongo.services.interfaces.StudentService;
import org.example.technihongo.services.interfaces.SystemFlashcardSetService;
import org.example.technihongo.services.interfaces.UserActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/system-flashcard-set")
public class SystemFlashcardSetController {
    @Autowired
    private SystemFlashcardSetService systemFlashcardSetService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentFlashcardSetProgressService studentFlashcardSetProgressService;
    @Autowired
    private UserActivityLogService userActivityLogService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> create(
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest httpRequest,
            @Valid @RequestBody SystemFlashcardSetRequestDTO requestDTO) {
        try {
            Integer userId = extractUserId(authorizationHeader);
            SystemFlashcardSetResponseDTO response = systemFlashcardSetService.create(userId, requestDTO);

            String ipAddress = httpRequest.getRemoteAddr();
            String userAgent = httpRequest.getHeader("User-Agent");
            userActivityLogService.trackUserActivityLog(
                    userId,
                    ActivityType.CREATE,
                    ContentType.SystemFlashcardSet,
                    response.getSystemSetId(),
                    ipAddress,
                    userAgent
            );

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
            HttpServletRequest httpRequest,
            @PathVariable Integer flashcardSetId,
            @Valid @RequestBody SystemFlashcardSetRequestDTO requestDTO) {
        try {
            Integer userId = extractUserId(authorizationHeader);
            SystemFlashcardSetResponseDTO response = systemFlashcardSetService.update(userId, flashcardSetId, requestDTO);

            String ipAddress = httpRequest.getRemoteAddr();
            String userAgent = httpRequest.getHeader("User-Agent");
            userActivityLogService.trackUserActivityLog(
                    userId,
                    ActivityType.UPDATE,
                    ContentType.SystemFlashcardSet,
                    response.getSystemSetId(),
                    ipAddress,
                    userAgent
            );

            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("System Flashcard Set updated successfully")
                    .data(response)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
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

    @PatchMapping("/updateOrder/{flashcardSetId}")
    public ResponseEntity<ApiResponse> updateFlashcardOrder(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer flashcardSetId,
            @Valid @RequestBody UpdateFlashcardOrderDTO requestDTO,
            HttpServletRequest httpRequest) {
        try {
            Integer userId = extractUserId(authorizationHeader);

            systemFlashcardSetService.updateFlashcardOrder(userId, flashcardSetId, requestDTO);
            String ipAddress = httpRequest.getRemoteAddr();
            String userAgent = httpRequest.getHeader("User-Agent");
            userActivityLogService.trackUserActivityLog(
                    userId,
                    ActivityType.UPDATE,
                    ContentType.SystemFlashcardSet,
                    flashcardSetId,
                    ipAddress,
                    userAgent
            );
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("System Flashcard Set order updated successfully")
                    .data(null)
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Invalid request: " + e.getMessage())
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
            HttpServletRequest httpRequest,
            @PathVariable Integer flashcardSetId) {
        try {
            Integer userId = extractUserId(authorizationHeader);
            systemFlashcardSetService.deleteSystemFlashcardSet(userId, flashcardSetId);

            String ipAddress = httpRequest.getRemoteAddr();
            String userAgent = httpRequest.getHeader("User-Agent");
            userActivityLogService.trackUserActivityLog(
                    userId,
                    ActivityType.DELETE,
                    ContentType.SystemFlashcardSet,
                    flashcardSetId,
                    ipAddress,
                    userAgent
            );

            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("System Flashcard Set marked as deleted successfully")
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
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

    @GetMapping("/getSysFlashcardSet/{systemFlashcardSetId}")
    public ResponseEntity<ApiResponse> getSystemFlashcardSetById(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer systemFlashcardSetId) {
        try {
            Integer userId = extractUserId(authorizationHeader);
            SystemFlashcardSetResponseDTO response = systemFlashcardSetService.getAllFlashcardsInSet(userId, systemFlashcardSetId);

            Integer studentId = studentService.getStudentIdByUserId(userId);
            if(studentId != null) {
                studentFlashcardSetProgressService.trackFlashcardSetProgress(studentId, systemFlashcardSetId, true, null);
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("System Flashcard Set retrieved successfully")
                    .data(response)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
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

    // HÀM NÀY KHÁC MÉO GÌ HÀM getSystemFlashcardSetById TRÊN ???
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
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

    private Integer extractUserId(String authorizationHeader) throws Exception {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return jwtUtil.extractUserId(token);
        }
        throw new UnauthorizedAccessException("Authorization failed! Invalid or missing token.");
    }
}