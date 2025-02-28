package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.*;
import org.example.technihongo.entities.LearningPath;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.LearningPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning-path")
@RequiredArgsConstructor
public class LearningPathController {
    @Autowired
    private LearningPathService learningPathService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllLearningPaths(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);

                if (roleId == 1 || roleId == 2) {
                    List<LearningPath> learningPaths = learningPathService.getAllLearningPaths();
                    if (learningPaths.isEmpty()) {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(false)
                                .message("List LearningPath is empty!")
                                .build());
                    } else {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Get All LearningPaths")
                                .data(learningPaths)
                                .build());
                    }
                } else {
                    List<LearningPath> learningPaths = learningPathService.getPublicLearningPaths();
                    if (learningPaths.isEmpty()) {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(false)
                                .message("List LearningPaths is empty!")
                                .build());
                    } else {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Get All Public LearningPaths")
                                .data(learningPaths)
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
    public ResponseEntity<ApiResponse> viewLearningPath(@PathVariable Integer id,
                                                  @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);

                if (roleId == 1 || roleId == 2) {
                    LearningPath learningPath = learningPathService.getLearningPathById(id);
                    if (learningPath == null) {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(false)
                                .message("LearningPath not found!")
                                .build());
                    } else {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Get LearningPath")
                                .data(learningPath)
                                .build());
                    }
                }
                else{
                    LearningPath learningPath = learningPathService.getPublicLearningPathById(id);
                    if(learningPath == null){
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(false)
                                .message("LearningPath not found!")
                                .build());
                    }else{
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Get LearningPath")
                                .data(learningPath)
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
    public ResponseEntity<ApiResponse> createLearningPath(@RequestBody CreateLearningPathDTO createLearningPathDTO,
                                                    @RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);

                LearningPath learningPath = learningPathService.createLearningPath(userId, createLearningPathDTO);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("LearningPath created successfully!")
                        .data(learningPath)
                        .build());
            }
            else throw new Exception("Authorization failed!");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to create LearningPath: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update/{pathId}")
    public ResponseEntity<ApiResponse> updateLearningPath(@PathVariable Integer pathId,
                                                    @RequestBody UpdateLearningPathDTO updateLearningPathDTO) {
        try{
            learningPathService.updateLearningPath(pathId, updateLearningPathDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("LearningPath updated successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update LearningPath: " + e.getMessage())
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
    public ResponseEntity<ApiResponse> searchLearningPathByTitle(@PathVariable String keyword) throws Exception {
        try{
            List<LearningPath> learningPaths = learningPathService.getLearningPathsByTitle(keyword);
            if(learningPaths.isEmpty()){
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("List LearningPaths is empty!")
                        .build());
            }else{
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get LearningPaths List By Keyword")
                        .data(learningPaths)
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteLearningPath(@PathVariable Integer id) {
        try{
            learningPathService.deleteLearningPath(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("LearningPath removed successfully!")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to delete LearningPath: " + e.getMessage())
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
