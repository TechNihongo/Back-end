package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CourseWithStudyPlanListDTO;
import org.example.technihongo.dto.CreateCourseStudyPlanDTO;
import org.example.technihongo.entities.CourseStudyPlan;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.CourseStudyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/course-study-plan")
@RequiredArgsConstructor
public class CourseStudyPlanController {
    @Autowired
    private CourseStudyPlanService courseStudyPlanService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCoursesWithStudyPlan() throws Exception {
        try{
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCourseWithStudyPlan(@PathVariable Integer id) throws Exception {
        try{
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCourseStudyPlan(@PathVariable Integer id) {
        try{
            courseStudyPlanService.deleteCourseStudyPlan(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("CourseStudyPlan removed successfully!")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to delete CourseStudyPlan: " + e.getMessage())
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
