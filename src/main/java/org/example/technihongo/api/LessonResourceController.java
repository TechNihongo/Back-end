package org.example.technihongo.api;

import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.*;
import org.example.technihongo.entities.LessonResource;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.LessonResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lesson-resource")
@Validated
public class LessonResourceController {
    @Autowired
    private LessonResourceService lessonResourceService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<ApiResponse> getLessonResourcesByLessonId(
            @PathVariable Integer lessonId,
            @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);

                List<LessonResource> lessonResourceList;
                if (roleId == 1 || roleId == 2) {
                    lessonResourceList = lessonResourceService.getLessonResourceListByLessonId(lessonId);
                } else {
                    lessonResourceList = lessonResourceService.getActiveLessonResourceListByLessonId(lessonId);
                }
                if (lessonResourceList.isEmpty()) {
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(false)
                            .message("List LessonResources is empty!")
                            .build());
                } else {
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Get All LessonResources By Lesson")
                            .data(lessonResourceList)
                            .build());
                }
            }
            else throw new Exception("Authorization failed!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Get LessonResources failed: " + e.getMessage())
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
    public ResponseEntity<ApiResponse> viewLessonResource(@PathVariable Integer id,
                                                          @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);

                LessonResource lessonResource;
                if (roleId == 1 || roleId == 2) {
                    lessonResource = lessonResourceService.getLessonResourceById(id);
                }
                else{
                    lessonResource = lessonResourceService.getActiveLessonResourceById(id);
                }
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get LessonResource")
                        .data(lessonResource)
                        .build());
            }
            else throw new Exception("Authorization failed!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Get LessonResource failed: " + e.getMessage())
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
    public ResponseEntity<ApiResponse> createLessonResource(@RequestBody CreateLessonResourceDTO createLessonResourceDTO){
        try {
            LessonResource lessonResource = lessonResourceService.createLessonResource(createLessonResourceDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("LessonResource created successfully!")
                    .data(lessonResource)
                    .build());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to create LessonResource: " + e.getMessage())
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
    public ResponseEntity<ApiResponse> updateLessonResource(@PathVariable Integer id,
                                                    @RequestBody UpdateLessonResourceDTO updateLessonResourceDTO) {
        try{
            lessonResourceService.updateLessonResource(id, updateLessonResourceDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("LessonResource updated successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update LessonResource: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update-order/{lessonId}")
    public ResponseEntity<ApiResponse> updateLessonResourceOrder(@PathVariable Integer lessonId,
                                                         @RequestBody UpdateLessonResourceOrderDTO updateLessonResourceOrderDTO) {
        try{
            lessonResourceService.updateLessonResourceOrder(lessonId, updateLessonResourceOrderDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("LessonResources updated successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update LessonResources: " + e.getMessage())
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
    public ResponseEntity<ApiResponse> deleteLessonResource(@PathVariable Integer id) {
        try{
            lessonResourceService.deleteLessonResource(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("LessonResource removed successfully!")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to delete LessonResource: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/study-plan")
    public ResponseEntity<ApiResponse> getLessonResourcesByDefaultStudyPlan(
            @RequestParam(defaultValue = "") Integer studyPlanId,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String type,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "lessonResourceId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try{
            PageResponseDTO<LessonResource> lessonResourceList = lessonResourceService
                    .getLessonResourcesByDefaultStudyPlanPaginated(studyPlanId, keyword, type, pageNo, pageSize, sortBy, sortDir);
            if (lessonResourceList.getContent().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("List LessonResources is empty!")
                        .build());
            } else {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .data(lessonResourceList)
                        .message("LessonResources retrieve successfully!")
                        .build());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to get LessonResources: " + e.getMessage())
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
