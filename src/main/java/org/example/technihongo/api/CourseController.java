package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CoursePublicDTO;
import org.example.technihongo.entities.Course;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCourses() throws Exception {
        List<Course> courseList = courseService.courseList();
        if(courseList.isEmpty()){
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(false)
                    .message("List courses is empty!")
                    .build());
        }else{
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Get All Courses")
                    .data(courseList)
                    .build());
        }
    }

    @GetMapping("/public/all")
    public ResponseEntity<ApiResponse> getPublicCourses() throws Exception {
        List<CoursePublicDTO> courseList = courseService.getPublicCourses();
        if(courseList.isEmpty()){
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(false)
                    .message("List courses is empty!")
                    .build());
        }else{
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Get All Public Courses")
                    .data(courseList)
                    .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> viewCourse(@PathVariable Integer id) throws Exception {
        Optional<Course> course = courseService.getCourseById(id);
        if(course.isEmpty()){
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(false)
                    .message("Course not found!")
                    .build());
        }else{
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Get Course")
                    .data(course)
                    .build());
        }
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<ApiResponse> viewPublicCourse(@PathVariable Integer id) throws Exception {
        Optional<CoursePublicDTO> course = courseService.getPublicCourseById(id);
        if(course.isEmpty()){
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(false)
                    .message("Course not found!")
                    .build());
        }else{
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Get Course")
                    .data(course)
                    .build());
        }
    }
}
