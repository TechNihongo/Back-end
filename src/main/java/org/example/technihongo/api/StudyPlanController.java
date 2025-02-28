package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.CreateStudyPlanDTO;
import org.example.technihongo.dto.UpdateStudyPlanDTO;
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
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse> getAllStudyPlansByCourseId(@RequestHeader("Authorization") String authorizationHeader,
                                                        @PathVariable Integer courseId) throws Exception {
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);

                if (roleId == 1 || roleId == 2) {
                    List<StudyPlan> studyPlanList = studyPlanService.getStudyPlanListByCourseId(courseId);
                    if (studyPlanList.isEmpty()) {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(false)
                                .message("List StudyPlans is empty!")
                                .build());
                    } else {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Get StudyPlans")
                                .data(studyPlanList)
                                .build());
                    }
                } else {
                    List<StudyPlan> studyPlanList = studyPlanService.getActiveStudyPlanListByCourseId(courseId);
                    if (studyPlanList.isEmpty()) {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(false)
                                .message("List StudyPlans is empty!")
                                .build());
                    } else {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Get Active StudyPlans")
                                .data(studyPlanList)
                                .build());
                    }
                }
            }
            else throw new Exception("Authorization failed!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to get StudyPlan: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> viewStudyPlan(@PathVariable Integer id,
                                                  @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);

                if (roleId == 1 || roleId == 2) {
                    StudyPlan StudyPlan = studyPlanService.getStudyPlanById(id);
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Get StudyPlan")
                            .data(StudyPlan)
                            .build());
                    }
                else{
                    StudyPlan StudyPlan = studyPlanService.getActiveStudyPlanById(id);
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Get StudyPlan")
                            .data(StudyPlan)
                            .build());
                    }
                }
            else throw new Exception("Authorization failed!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                        .success(false)
                        .message("Failed to get StudyPlan: " + e.getMessage())
                        .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }



    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createStudyPlan(@RequestBody CreateStudyPlanDTO createStudyPlanDTO) {
        try {
                StudyPlan StudyPlan = studyPlanService.createStudyPlan(createStudyPlanDTO);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("StudyPlan created successfully!")
                        .data(StudyPlan)
                        .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to create StudyPlan: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateStudyPlan(@PathVariable Integer id,
                                                    @RequestBody UpdateStudyPlanDTO updateStudyPlanDTO) {
        try{
            studyPlanService.updateStudyPlan(id, updateStudyPlanDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("StudyPlan updated successfully!")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update StudyPlan: " + e.getMessage())
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
                    .message("StudyPlan deleted successfully!")
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
