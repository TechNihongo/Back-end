package org.example.technihongo.api;


import org.example.technihongo.dto.EnrollStudyPlanRequest;
import org.example.technihongo.dto.StudentStudyPlanDTO;
import org.example.technihongo.dto.StudyPlanDTO;
import org.example.technihongo.dto.SwitchStudyPlanRequestDTO;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.StudentStudyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/student-study-plan")
public class StudentStudyPlanController {
    @Autowired
    private StudentStudyPlanService studentStudyPlanService;

    @PostMapping("/enroll")
    public ResponseEntity<ApiResponse> enrollStudentInStudyPlan(@RequestBody EnrollStudyPlanRequest request) {
        try {
            StudentStudyPlanDTO enrolledPlan = studentStudyPlanService.enrollStudentInStudyPlan(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Student successfully enrolled in study plan")
                    .data(enrolledPlan)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/switchStudyPlan")
    public ResponseEntity<ApiResponse> switchStudyPlan(@RequestBody SwitchStudyPlanRequestDTO request) {
        try {
            StudentStudyPlanDTO newPlan = studentStudyPlanService.switchStudyPlan(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Study plan successfully switched")
                    .data(newPlan)
                    .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (IllegalStateException e) {
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

    @GetMapping("/availableStudyPlan/{studentId}")
    public ResponseEntity<ApiResponse> getAvailableStudyPlan(@PathVariable Integer studentId) {
        try {
            List<StudyPlanDTO> availablePlans = studentStudyPlanService.getAvailableStudyPlans(studentId);
            if (availablePlans.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("No available study plans found for student")
                        .build());
            } else {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Available study plans retrieved successfully")
                        .data(availablePlans)
                        .build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/activeStudyPlan/{studentId}")
    public ResponseEntity<ApiResponse> getActiveStudyPlan(@PathVariable Integer studentId) {
        try {
            StudentStudyPlanDTO activePlan = studentStudyPlanService.getActiveStudyPlan(studentId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Active study plan retrieved successfully")
                    .data(activePlan)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/historyStudyPlan/{studentId}")
    public ResponseEntity<ApiResponse> getStudyPlanHistory(@PathVariable Integer studentId) {
        try {
            List<StudentStudyPlanDTO> planHistory = studentStudyPlanService.getStudyPlanHistory(studentId);
            if (planHistory.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("No study plan history found for student")
                        .build());
            } else {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Study plan history retrieved successfully")
                        .data(planHistory)
                        .build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }
}
