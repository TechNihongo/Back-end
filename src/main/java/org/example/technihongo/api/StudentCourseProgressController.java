package org.example.technihongo.api;

import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.CourseStatisticsDTO;
import org.example.technihongo.entities.StudentCourseProgress;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.StudentCourseProgressService;
import org.example.technihongo.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-progress")
public class StudentCourseProgressController {
    @Autowired
    private StudentCourseProgressService courseProgressService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private StudentService studentService;

    @GetMapping("/view/{studentId}")
    public ResponseEntity<ApiResponse> getStudentCourseProgress(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer studentId,
            @RequestParam Integer courseId) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer loginStudentId = studentService.getStudentIdByUserId(userId);

                StudentCourseProgress progress;
                if(loginStudentId != null && loginStudentId.equals(studentId)){
                    progress = courseProgressService.getStudentCourseProgress(studentId, courseId);
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Student course progress retrieved successfully")
                            .data(progress)
                            .build());
                }
                else if(loginStudentId != null){
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(ApiResponse.builder()
                            .success(false)
                            .message("Student can not view others' progress!")
                            .build());
                }
                else {
                    progress = courseProgressService.getStudentCourseProgress(studentId, courseId);
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Student course progress retrieved successfully")
                            .data(progress)
                            .build());
                }
            } else {
                throw new Exception("Authorization failed!");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve student course progress: " + e.getMessage())
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
    public ResponseEntity<ApiResponse> getAllStudentCourseProgress(
            @PathVariable Integer studentId) {
        try {
            List<StudentCourseProgress> progressList = courseProgressService.getAllStudentCourseProgress(studentId);

            if (progressList.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("No course progress found for this student")
                        .build());
            } else {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Student course progress retrieved successfully")
                        .data(progressList)
                        .build());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve student course progress: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/statistics/{courseId}")
    public ResponseEntity<ApiResponse> viewCourseStatistics(
            @PathVariable Integer courseId) {
        try {
            CourseStatisticsDTO statistics = courseProgressService.viewCourseStatistics(courseId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Course statistics retrieved successfully")
                    .data(statistics)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve course statistics: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/enroll")
    public ResponseEntity<ApiResponse> enrollCourse(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam Integer courseId) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);

                courseProgressService.enrollCourse(studentId, courseId);

                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Course enrolled successfully")
                        .data(null)
                        .build());
            } else {
                throw new Exception("Authorization failed!");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to enroll course: " + e.getMessage())
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
