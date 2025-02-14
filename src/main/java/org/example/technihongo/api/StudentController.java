package org.example.technihongo.api;


import org.example.technihongo.dto.DailyGoalRequest;
import org.example.technihongo.dto.DifficultyLevelRequest;
import org.example.technihongo.dto.UpdateProfileDTO;
import org.example.technihongo.enums.DifficultyLevelEnum;
import org.example.technihongo.exception.InvalidDifficultyLevelException;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@Validated
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PatchMapping("/{studentId}/daily-goal")
    public ResponseEntity<ApiResponse> setDailyGoal(
            @PathVariable Integer studentId,
            @RequestBody DailyGoalRequest request) {
        try {
            UpdateProfileDTO updatedStudent = studentService.setDailyGoal(studentId, request.getDailyGoal());
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Daily goal updated successfully")
                    .data(updatedStudent)
                    .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Student not found: " + e.getMessage())
                            .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Invalid daily goal: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update daily goal: " + e.getMessage())
                            .build());
        }
    }


    @PatchMapping("/{studentId}/difficulty-level")
    public ResponseEntity<ApiResponse> updateDifficultyLevel(
            @PathVariable Integer studentId,
            @RequestBody DifficultyLevelRequest request) {
        try {
            DifficultyLevelEnum level = DifficultyLevelEnum.valueOf(request.getDifficultyLevel());
            UpdateProfileDTO updatedStudent = studentService.updateDifficultyLevel(studentId, level);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Difficulty level updated successfully")
                    .data(updatedStudent)
                    .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Student not found: " + e.getMessage())
                            .build());
        } catch (InvalidDifficultyLevelException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Invalid difficulty level: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update difficulty level: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/{userId}/profile")
    public ResponseEntity<ApiResponse> updateProfile(
            @PathVariable Integer userId,
            @RequestBody UpdateProfileDTO request) {
        try {
            studentService.updateStudentProfile(userId, request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Profile updated successfully")
                    .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Student not found: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update profile: " + e.getMessage())
                            .build());
        }
    }




}