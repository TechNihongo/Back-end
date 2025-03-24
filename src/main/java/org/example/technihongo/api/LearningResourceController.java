package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.LearningResourceDTO;
import org.example.technihongo.dto.LearningResourceStatusDTO;
import org.example.technihongo.entities.LearningResource;;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.LearningResourceService;
import org.example.technihongo.services.interfaces.StudentResourceProgressService;
import org.example.technihongo.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning-resource")
@RequiredArgsConstructor
public class LearningResourceController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private LearningResourceService learningResourceService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentResourceProgressService studentResourceProgressService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllLearningResources() throws Exception {
        try{
            List<LearningResource> learningResourceList = learningResourceService.getAllLearningResources();
            if (learningResourceList.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("List LearningResources is empty!")
                        .build());
            } else {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get All LearningResources")
                        .data(learningResourceList)
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
    public ResponseEntity<ApiResponse> viewLearningResource(@PathVariable Integer id,
                                                @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);

                if (roleId == 1 || roleId == 2) {
                    LearningResource learningResource = learningResourceService.getLearningResourceById(id);
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Get LearningResource")
                            .data(learningResource)
                            .build());
                }
                else{
                    LearningResource learningResource = learningResourceService.getPublicLearningResourceById(userId, id);

                    if(studentId != null){
                        studentResourceProgressService.trackLearningResourceProgress(studentId, id, null);
                    }

                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Get LearningResource")
                            .data(learningResource)
                            .build());
                }
            }
             else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Unauthorized")
                                .build());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to get LearningResource: " + e.getMessage())
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
    public ResponseEntity<ApiResponse> createLearningResource(@RequestBody LearningResourceDTO learningResourceDTO,
                                                  @RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);

                LearningResource learningResource = learningResourceService.createLearningResource(userId, learningResourceDTO);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("LearningResource created successfully!")
                        .data(learningResource)
                        .build());
            }
             else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Unauthorized")
                                .build());
            }

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to create LearningResource: " + e.getMessage())
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
    public ResponseEntity<ApiResponse> updateLearningResource(@PathVariable Integer id,
                                                  @RequestBody LearningResourceDTO learningResourceDTO) {
        try{
            learningResourceService.updateLearningResource(id, learningResourceDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("LearningResource updated successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update LearningResource: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update-status/{id}")
    public ResponseEntity<ApiResponse> updateLearningResourceStatus(@PathVariable Integer id,
                                                        @RequestBody LearningResourceStatusDTO learningResourceStatusDTO) {
        try{
            learningResourceService.updateLearningResourceStatus(id, learningResourceStatusDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("LearningResource updated successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update LearningResource: " + e.getMessage())
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
    public ResponseEntity<ApiResponse> deleteLearningResource(@PathVariable Integer id) {
        try{
            learningResourceService.deleteLearningResource(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("LearningResource removed successfully!")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to delete LearningResource: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/creator")
    public ResponseEntity<ApiResponse> getLearningResourceListByCreator(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);

                List<LearningResource> learningResourceList = learningResourceService.getListLearningResourcesByCreatorId(userId);
                if (learningResourceList.isEmpty()) {
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(false)
                            .message("List LearningResources is empty!")
                            .build());
                } else {
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Get LearningResources List By Creator")
                            .data(learningResourceList)
                            .build());
                }
            }
             else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Unauthorized")
                                .build());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to get LearningResources: " + e.getMessage())
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
