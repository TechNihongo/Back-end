package org.example.technihongo.api;

import org.example.technihongo.entities.Question;
import org.example.technihongo.entities.StudentLearningStatistics;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.StudentLearningStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/statistics")
public class StudentLearningStatisticsController {
    @Autowired
    private StudentLearningStatisticsService studentLearningStatisticsService;

    @GetMapping("/view")
    public ResponseEntity<ApiResponse> viewStudentLearningStatistics(
            @RequestParam Integer studentId){
        try{
            StudentLearningStatistics statistics = studentLearningStatisticsService.viewStudentLearningStatistics(studentId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Get Student LearningStatistics")
                    .data(statistics)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Get Student LearningStatistics failed: " + e.getMessage())
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
