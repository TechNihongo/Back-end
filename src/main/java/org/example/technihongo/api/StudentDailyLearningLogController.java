package org.example.technihongo.api;

import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.CoursePublicDTO;
import org.example.technihongo.entities.Course;
import org.example.technihongo.entities.StudentDailyLearningLog;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.StudentDailyLearningLogService;
import org.example.technihongo.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/learning-log")
public class StudentDailyLearningLogController {
    @Autowired
    private StudentDailyLearningLogService studentDailyLearningLogService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private StudentService studentService;

    @PostMapping("/track")
    public ResponseEntity<ApiResponse> trackStudentDailyLearningLog(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(defaultValue = "0") Integer studyTime){
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);

                studentDailyLearningLogService.trackStudentDailyLearningLog(studentId, studyTime);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("Daily Learning Log Recorded Successfully!")
                        .build());
            }
            else throw new Exception("Authorization failed!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Recorded Daily Learning Log Failed: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/view")
    public ResponseEntity<ApiResponse> getStudentDailyLearningLog(
            @RequestHeader("Authorization") String authorizationHeader){
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);

                StudentDailyLearningLog log = studentDailyLearningLogService.getStudentDailyLearningLog(studentId);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .data(log)
                        .message("Daily Learning Log Retrieved Successfully!")
                        .build());
            }
            else throw new Exception("Authorization failed!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Get Daily Learning Log Failed: " + e.getMessage())
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
