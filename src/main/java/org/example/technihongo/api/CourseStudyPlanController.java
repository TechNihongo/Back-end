package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CourseWithStudyPlanListDTO;
import org.example.technihongo.dto.CreateCourseStudyPlanDTO;
import org.example.technihongo.entities.Course;
import org.example.technihongo.entities.CourseStudyPlan;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.CourseStudyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/course_sp")
@RequiredArgsConstructor
public class CourseStudyPlanController {
    @Autowired
    private CourseStudyPlanService courseStudyPlanService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCoursesWithStudyPlan() throws Exception {
        List<CourseWithStudyPlanListDTO> courseDTOS = courseStudyPlanService.getCourseListWithStudyPlans();
        if(courseDTOS.isEmpty()){
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(false)
                    .message("List courses is empty!")
                    .build());
        }else{
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Get All Courses With Study Plan")
                    .data(courseDTOS)
                    .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCourseWithStudyPlan(@PathVariable Integer id) throws Exception {
        Optional<CourseWithStudyPlanListDTO> courseDTO = courseStudyPlanService.getCourseWithStudyPlans(id);
        if(courseDTO.isEmpty()){
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(false)
                    .message("Course not found!")
                    .build());
        }else{
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Get Course With Study Plan")
                    .data(courseDTO)
                    .build());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> AddCourseStudyPlan(@RequestBody CreateCourseStudyPlanDTO createCourseStudyPlanDTO) throws Exception {
        try{
            CourseStudyPlan courseStudyPlan = courseStudyPlanService.createCourseStudyPlan(createCourseStudyPlanDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Add Study plan into Course successfully!")
                    .data(courseStudyPlan)
                    .build());

        }catch (Exception e){
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(false)
                    .message("Create CourseStudyPlan fail! Error: " + e.getMessage())
                    .build());
        }
    }
}
