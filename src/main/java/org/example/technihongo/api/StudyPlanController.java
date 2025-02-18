package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CreateLessonDTO;
import org.example.technihongo.dto.CreateStudyPlanDTO;
import org.example.technihongo.dto.UpdateLessonDTO;
import org.example.technihongo.dto.UpdateStudyPlanDTO;
import org.example.technihongo.entities.Course;
import org.example.technihongo.entities.Lesson;
import org.example.technihongo.entities.StudyPlan;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.StudyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/study-plan")
@RequiredArgsConstructor
public class StudyPlanController {
    @Autowired
    private StudyPlanService studyPlanService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllStudyPlans() throws Exception {
        try{
            List<StudyPlan> studyPlanList = studyPlanService.studyPlanList();
            if(studyPlanList.isEmpty()){
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("List study plans is empty!")
                        .build());
            }else{
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get All Study Plans")
                        .data(studyPlanList)
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> viewStudyPlan(@PathVariable Integer id) throws Exception {
        try{
            Optional<StudyPlan> studyPlan = studyPlanService.getStudyPlan(id);
            if(studyPlan.isEmpty()){
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("Study Plan not found!")
                        .build());
            }else{
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get Study Plan")
                        .data(studyPlan)
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

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse> viewStudyPlanListInCourse(@PathVariable Integer courseId) throws Exception {
        try{
            List<StudyPlan> studyPlans = studyPlanService.getActiveStudyPlansByCourseId(courseId);
            if(studyPlans.isEmpty()){
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("Study Plan List not found!")
                        .build());
            }else{
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get Study Plan List")
                        .data(studyPlans)
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

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createStudyPlan(@RequestBody CreateStudyPlanDTO createStudyPlanDTO){
        try {
            StudyPlan studyPlan = studyPlanService.createStudyPlan(createStudyPlanDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Study Plan created successfully!")
                    .data(studyPlan)
                    .build());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to create Study Plan: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update/{planId}")
    public ResponseEntity<ApiResponse> updateStudyPlan(@PathVariable Integer planId,
                                                    @RequestBody UpdateStudyPlanDTO updateStudyPlanDTO) {
        try{
            studyPlanService.updateStudyPlan(planId, updateStudyPlanDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Study Plan updated successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update Study Plan: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteStudyPlan(@PathVariable Integer id) {
        try{
            studyPlanService.deleteStudyPlan(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("StudyPlan removed successfully!")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to delete StudyPlan: " + e.getMessage())
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
