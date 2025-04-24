package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.entities.Achievement;
import org.example.technihongo.entities.StudentAchievement;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.AchievementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/achievement")
@RequiredArgsConstructor
public class AchievementController {
    private final AchievementService achievementService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllAchievement() {
        try{
            List<Achievement> achievementList = achievementService.achievementList();
            if(achievementList.isEmpty()){
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("List Achievement is empty!")
                        .build());
            }else{
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get All Achievement")
                        .data(achievementList)
                        .build());
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
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
    @GetMapping("/student/achievement/{studentId}")
    @PreAuthorize("hasRole('ROLE_Student')")
    public ResponseEntity<ApiResponse> getStudentAchievements(@PathVariable Integer studentId) {
        try {
            List<StudentAchievement> studentAchievements = achievementService.getStudentAchievements(studentId);
            if (studentAchievements.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("No achievements found for student ID: " + studentId)
                        .build());
            } else {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get Achievements for Student ID: " + studentId)
                        .data(studentAchievements)
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



    @PostMapping("/student/track-achievement/{studentId}")
    @PreAuthorize("hasRole('ROLE_Student')")
    public ResponseEntity<ApiResponse> trackAchievements(@PathVariable Integer studentId) {
        try {
            achievementService.trackAchievementProgress(studentId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Tracked achievements for Student ID: " + studentId)
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
