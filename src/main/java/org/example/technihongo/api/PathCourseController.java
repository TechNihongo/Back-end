package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.CreatePathCourseDTO;
import org.example.technihongo.dto.PageResponseDTO;
import org.example.technihongo.dto.UpdatePathCourseOrderDTO;
import org.example.technihongo.entities.PathCourse;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.LearningPathService;
import org.example.technihongo.services.interfaces.PathCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/path-course")
@RequiredArgsConstructor
public class PathCourseController {
    @Autowired
    private PathCourseService pathCourseService;
    @Autowired
    private LearningPathService learningPathService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/learning-path/{pathId}")
    public ResponseEntity<ApiResponse> getPathCourseListByLearningPathId(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer pathId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "100") int pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);

                if (roleId == 1 || roleId == 2) {
                    PageResponseDTO<PathCourse> pathCourses = pathCourseService.getPathCoursesByLearningPathId(pathId, pageNo, pageSize, sortBy, sortDir);
                    if (pathCourses.getContent().isEmpty()) {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(false)
                                .message("List PathCourse is empty!")
                                .build());
                    } else {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Get PathCourse List")
                                .data(pathCourses)
                                .build());
                    }
                }
                else{
                    PageResponseDTO<PathCourse> pathCourses = pathCourseService.getPublicPathCourseListByLearningPathId(pathId, pageNo, pageSize, sortBy, sortDir);
                    if (pathCourses.getContent().isEmpty()) {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(false)
                                .message("List PathCourse is empty!")
                                .build());
                    } else {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Get Public PathCourse List")
                                .data(pathCourses)
                                .build());
                    }
                }
            } else {
                throw new Exception("Authorization failed!");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to get PathCourses: " + e.getMessage())
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
    public ResponseEntity<ApiResponse> getPathCourseById(@PathVariable Integer id) throws Exception {
        try{
            PathCourse pathCourse = pathCourseService.getPathCourseById(id);
            if(pathCourse == null){
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("PathCourse not found!")
                        .build());
            }else{
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get PathCourse")
                        .data(pathCourse)
                        .build());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to get PathCourse: " + e.getMessage())
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
    public ResponseEntity<ApiResponse> createPathCourse(@RequestBody CreatePathCourseDTO createPathCourseDTO){
        try {
            PathCourse pathCourse = pathCourseService.createPathCourse(createPathCourseDTO);
            learningPathService.updateTotalCourses(createPathCourseDTO.getPathId());
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("PathCourse created successfully!")
                    .data(pathCourse)
                    .build());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to create PathCourse: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update-order/{pathId}")
    public ResponseEntity<ApiResponse> updatePathCourseOrder(@PathVariable Integer pathId,
                                                         @RequestBody UpdatePathCourseOrderDTO updatePathCourseOrderDTO) {
        try{
            pathCourseService.updatePathCourseOrder(pathId, updatePathCourseOrderDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("PathCourse updated successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update PathCourse: " + e.getMessage())
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
    public ResponseEntity<ApiResponse> deletePathCourse(@PathVariable Integer id) {
        try{
            Integer pathId = pathCourseService.getPathCourseById(id).getLearningPath().getPathId();
            pathCourseService.deletePathCourse(id);
            learningPathService.updateTotalCourses(pathId);

            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("PathCourse removed successfully!")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to delete PathCourse: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/set-order/{pathId}")
    public ResponseEntity<ApiResponse> setPathCourseOrder(@PathVariable Integer pathId,
                                                          @RequestParam Integer pathCourseId,
                                                          @RequestParam Integer newOrder) {
        try{
            pathCourseService.setPathCourseOrder(pathId, pathCourseId, newOrder);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("PathCourse updated successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update PathCourse: " + e.getMessage())
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
