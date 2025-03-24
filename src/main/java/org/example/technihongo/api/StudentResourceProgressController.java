package org.example.technihongo.api;

import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.entities.StudentResourceProgress;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.StudentResourceProgressService;
import org.example.technihongo.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse> trackLearningResourceProgress(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam Integer resourceId,
            @RequestParam(required = false) String notes) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);

                resourceProgressService.trackLearningResourceProgress(studentId, resourceId, notes);

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

    @GetMapping("/all/{studentId}")
    public ResponseEntity<ApiResponse> getAllStudentResourceProgress(
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
            @PathVariable Integer studentId,
            @RequestParam Integer resourceId) {
        try {
            StudentResourceProgress progress = resourceProgressService.viewStudentResourceProgress(studentId, resourceId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Learning resource progress retrieved successfully")
                    .data(progress)
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
}
