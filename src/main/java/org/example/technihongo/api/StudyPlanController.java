package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.entities.Course;
import org.example.technihongo.entities.StudyPlan;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.StudyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> viewStudyPlan(@PathVariable Integer id) throws Exception {
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
    }
}
