package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.*;
import org.example.technihongo.entities.Course;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCourses(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);

                if (roleId == 1 || roleId == 2) {
                    List<Course> courseList = courseService.courseList();
                    if (courseList.isEmpty()) {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(false)
                                .message("List courses is empty!")
                                .build());
                    } else {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Get All Courses")
                                .data(courseList)
                                .build());
                    }
                } else {
                    List<CoursePublicDTO> courseList = courseService.getPublicCourses();
                    if (courseList.isEmpty()) {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(false)
                                .message("List courses is empty!")
                                .build());
                    } else {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Get All Public Courses")
                                .data(courseList)
                                .build());
                    }
                }
            }
            else throw new Exception("Authorization failed!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> viewCourse(@PathVariable Integer id,
                                                  @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);

                if (roleId == 1 || roleId == 2) {
                    Optional<Course> course = courseService.getCourseById(id);
                    if (course.isEmpty()) {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(false)
                                .message("Course not found!")
                                .build());
                    } else {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Get Course")
                                .data(course)
                                .build());
                    }
                }
                else{
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
            else throw new Exception("Authorization failed!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCourse(@RequestBody CreateCourseDTO createCourseDTO,
                                                    @RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);

                Course course = courseService.createCourse(userId, createCourseDTO);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Course created successfully!")
                        .data(course)
                        .build());
            }
            else throw new Exception("Authorization failed!");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to create course: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update/{courseId}")
    public ResponseEntity<ApiResponse> updateCourse(@PathVariable Integer courseId,
                                                    @RequestBody UpdateCourseDTO updateCourseDTO) {
        try{
            courseService.updateCourse(courseId, updateCourseDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Course updated successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update course: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<ApiResponse> searchCourseByTitle(@PathVariable String keyword) throws Exception {
        try{
            List<Course> course = courseService.searchCourseByTitle(keyword);
            if(course.isEmpty()){
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("List courses is empty!")
                        .build());
            }else{
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get Courses List By Keyword")
                        .data(course)
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

    @GetMapping("/creator")
    public ResponseEntity<ApiResponse> getCourseListByCreator(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);

                List<Course> course = courseService.getListCoursesByCreatorId(userId);
                if (course.isEmpty()) {
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(false)
                            .message("List courses is empty!")
                            .build());
                } else {
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Get Courses List By Creator")
                            .data(course)
                            .build());
                }
            }
            else throw new Exception("Authorization failed!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to get courses: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/all/paginated")
    public ResponseEntity<ApiResponse> getAllCoursesPaginated(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "courseId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") Integer domainId) throws Exception {
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);

                if (roleId == 1 || roleId == 2) {
                    PageResponseDTO<Course> courseList = courseService.courseListPaginated(keyword, domainId, pageNo, pageSize, sortBy, sortDir);
                    if (courseList.getContent().isEmpty()) {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(false)
                                .message("List courses is empty!")
                                .build());
                    } else {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Get All Courses")
                                .data(courseList)
                                .build());
                    }
                } else {
                    PageResponseDTO<Course> courseList = courseService.getPublicCoursesPaginated(keyword, domainId, pageNo, pageSize, sortBy, sortDir);
                    if (courseList.getContent().isEmpty()) {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(false)
                                .message("List courses is empty!")
                                .build());
                    } else {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Get All Public Courses")
                                .data(courseList)
                                .build());
                    }
                }
            }
            else throw new Exception("Authorization failed!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to get courses: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/search/paginated/{keyword}")
    public ResponseEntity<ApiResponse> searchCourseByTitlePaginated(
            @PathVariable String keyword,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "courseId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) throws Exception {
        try{
            PageResponseDTO<Course> courseList = courseService.searchCourseByTitlePaginated(keyword, pageNo, pageSize, sortBy, sortDir);
            if(courseList.getContent().isEmpty()){
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("List courses is empty!")
                        .build());
            }else{
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get Courses List By Keyword")
                        .data(courseList)
                        .build());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to get courses: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/creator/paginated")
    public ResponseEntity<ApiResponse> getCourseListByCreatorPaginated(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "courseId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") Integer domainId) throws Exception {
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);

                PageResponseDTO<Course> courseList = courseService.getListCoursesByCreatorIdPaginated(keyword, domainId, userId, pageNo, pageSize, sortBy, sortDir);
                if (courseList.getContent().isEmpty()) {
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(false)
                            .message("List courses is empty!")
                            .build());
                } else {
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Get Courses List By Creator")
                            .data(courseList)
                            .build());
                }
            }
            else throw new Exception("Authorization failed!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to get courses: " + e.getMessage())
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
