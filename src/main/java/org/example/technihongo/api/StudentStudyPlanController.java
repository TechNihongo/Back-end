package org.example.technihongo.api;


import jakarta.servlet.http.HttpServletRequest;
import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.EnrollStudyPlanRequest;
import org.example.technihongo.dto.StudentStudyPlanDTO;
import org.example.technihongo.dto.StudyPlanDTO;
import org.example.technihongo.dto.SwitchStudyPlanRequestDTO;
import org.example.technihongo.entities.Course;
import org.example.technihongo.enums.ActivityType;
import org.example.technihongo.enums.ContentType;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.*;
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
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserActivityLogService userActivityLogService;
    @Autowired
    private StudentCourseProgressService studentCourseProgressService;
    @Autowired
    private StudyPlanService studyPlanService;
    @Autowired
    private StudentService studentService;

    @PostMapping("/enroll")
    public ResponseEntity<ApiResponse> enrollStudentInStudyPlan(
            @RequestBody EnrollStudyPlanRequest request,
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest httpRequest) {
        try {
            StudentStudyPlanDTO enrolledPlan = studentStudyPlanService.enrollStudentInStudyPlan(request);

            String ipAddress = httpRequest.getRemoteAddr();
            String userAgent = httpRequest.getHeader("User-Agent");
            userActivityLogService.trackUserActivityLog(
                    extractUserId(authorizationHeader),
                    ActivityType.ENROLL,
                    ContentType.StudentStudyPlan,
                    enrolledPlan.getStudentPlanId(),
                    ipAddress,
                    userAgent
            );

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
    public ResponseEntity<ApiResponse> switchStudyPlan(
            @RequestBody SwitchStudyPlanRequestDTO request,
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest httpRequest) {
        try {
            StudentStudyPlanDTO newPlan = studentStudyPlanService.switchStudyPlan(request);
            Course course = studyPlanService.getCourseByStudyPlanId(request.getNewStudyPlanId());
            studentCourseProgressService.trackStudentCourseProgress(request.getStudentId(), course.getCourseId(), null);

            String ipAddress = httpRequest.getRemoteAddr();
            String userAgent = httpRequest.getHeader("User-Agent");
            userActivityLogService.trackUserActivityLog(
                    extractUserId(authorizationHeader),
                    ActivityType.SWITCH,
                    ContentType.StudentStudyPlan,
                    newPlan.getStudentPlanId(),
                    ipAddress,
                    userAgent
            );

            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Đổi StudyPlan thành công!")
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

    @GetMapping("/activeStudyPlan/{courseId}")
    public ResponseEntity<ApiResponse> getActiveStudyPlan(
            @PathVariable Integer courseId,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            Integer studentId = extractStudentId(authorizationHeader);
            StudentStudyPlanDTO activePlan = studentStudyPlanService.getActiveStudyPlan(studentId, courseId);
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

    private Integer extractUserId(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return jwtUtil.extractUserId(token);
        }
        throw new IllegalArgumentException("Authorization header is missing or invalid.");
    }

    private Integer extractStudentId(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            Integer userId = jwtUtil.extractUserId(token);
            return studentService.getStudentIdByUserId(userId);
        }
        throw new IllegalArgumentException("Authorization header is missing or invalid.");
    }
}
