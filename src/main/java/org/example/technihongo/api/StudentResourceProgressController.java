package org.example.technihongo.api;

import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.NoteDTO;
import org.example.technihongo.entities.StudentResourceProgress;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.StudentResourceProgressService;
import org.example.technihongo.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resource-progress")
public class StudentResourceProgressController {
    @Autowired
    private StudentResourceProgressService resourceProgressService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private StudentService studentService;

    @PatchMapping("/track")
    @PreAuthorize("hasRole('ROLE_Student')")
    public ResponseEntity<ApiResponse> trackLearningResourceProgress(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam Integer resourceId) {
//            @RequestParam(required = false) String notes) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);

                resourceProgressService.trackLearningResourceProgress(studentId, resourceId, null);

                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Learning resource progress tracked successfully")
                        .data(null)
                        .build());
            }  else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Unauthorized")
                                .build());
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to track learning resource progress: " + e.getMessage())
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
    @PreAuthorize("hasRole('ROLE_Student')")
    public ResponseEntity<ApiResponse> completeLearningResourceProgress(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam Integer resourceId) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);

                resourceProgressService.completeLearningResourceProgress(studentId, resourceId);

                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Learning resource progress completed successfully!")
                        .data(null)
                        .build());
            }  else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Unauthorized")
                                .build());
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to complete learning resource progress: " + e.getMessage())
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
    public ResponseEntity<ApiResponse> getAllStudentResourceProgress(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer studentId) {
        try {
            List<StudentResourceProgress> progressList = resourceProgressService.getAllStudentResourceProgress(studentId);

            if (progressList.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("No learning resource progress found for this student")
                        .build());
            } else {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Learning resource progress retrieved successfully")
                        .data(progressList)
                        .build());
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve learning resource progress: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/view/{studentId}")
    public ResponseEntity<ApiResponse> viewStudentResourceProgress(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer studentId,
            @RequestParam Integer resourceId) {
        try {
            StudentResourceProgress progress = resourceProgressService.viewStudentResourceProgress(studentId, resourceId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Learning resource progress retrieved successfully")
                    .data(progress)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve learning resource progress: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/note")
    @PreAuthorize("hasRole('ROLE_Student')")
    public ResponseEntity<ApiResponse> writeNoteForLearningResource(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam Integer resourceId,
            @RequestBody NoteDTO note) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);

                StudentResourceProgress progress = resourceProgressService.writeNoteForLearningResource(studentId, resourceId, note.getNote());

                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Ghi chú thành công!")
                        .data(progress)
                        .build());
            }  else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Không có quyền")
                                .build());
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to track learning resource progress: " + e.getMessage())
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
