package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.*;
import org.example.technihongo.entities.Lesson;
import org.example.technihongo.entities.StudentLessonProgress;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.LessonService;
import org.example.technihongo.services.interfaces.StudentCourseProgressService;
import org.example.technihongo.services.interfaces.StudentLessonProgressService;
import org.example.technihongo.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lesson")
@RequiredArgsConstructor
public class LessonController {
    @Autowired
    private LessonService lessonService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentCourseProgressService studentCourseProgressService;
    @Autowired
    private StudentLessonProgressService studentLessonProgressService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getLessonById(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authorizationHeader){
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);

                if(studentId != null) {
                    lessonService.checkLessonProgressPrerequisite(studentId, id);
                    studentLessonProgressService.trackStudentLessonProgress(studentId, id);
                    Integer courseId = lessonService.getCourseIdByLessonId(id);
                    studentCourseProgressService.trackStudentCourseProgress(studentId, courseId, id);
                }

                Optional<Lesson> lesson = lessonService.getLessonById(id);

                if(lesson.isEmpty()){
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(false)
                            .message("Lesson not found!")
                            .build());
                }else{
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Get Lesson")
                            .data(lesson)
                            .build());
                }
            } else {
                throw new Exception("Authorization failed!");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/study-plan/{id}")
    public ResponseEntity<ApiResponse> getLessonListByStudyPlanId(@PathVariable Integer id) throws Exception {
        try{
            List<Lesson> lessonList = lessonService.getLessonListByStudyPlanId(id);
            if(lessonList.isEmpty()){
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("List lessons is empty!")
                        .build());
            }else{
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get Lesson List")
                        .data(lessonList)
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

    @GetMapping("/study-plan/paginated/{id}")
    public ResponseEntity<ApiResponse> getLessonListByStudyPlanIdPaginated(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "lessonId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "") String keyword) throws Exception {
        try{
            PageResponseDTO<Lesson> lessonList = lessonService.getLessonListByStudyPlanIdPaginated(id, pageNo, pageSize, sortBy, sortDir, keyword);
            if(lessonList.getContent().isEmpty()){
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("List lessons is empty!")
                        .build());
            }else{
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get Lesson List")
                        .data(lessonList)
                        .build());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to get lessons: " + e.getMessage())
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
    public ResponseEntity<ApiResponse> createLesson(@RequestBody CreateLessonDTO createLessonDTO){
        try {
                Lesson lesson = lessonService.createLesson(createLessonDTO);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Lesson created successfully!")
                        .data(lesson)
                        .build());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to create lesson: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update/{lessonId}")
    public ResponseEntity<ApiResponse> updateLesson(@PathVariable Integer lessonId,
                                                    @RequestBody UpdateLessonDTO updateLessonDTO) {
        try{
            lessonService.updateLesson(lessonId, updateLessonDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Lesson updated successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update lesson: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update-order/{studyPlanId}")
    public ResponseEntity<ApiResponse> updateLessonOrder(@PathVariable Integer studyPlanId,
                                                    @RequestBody UpdateLessonOrderDTO updateLessonOrderDTO) {
        try{
            lessonService.updateLessonOrder(studyPlanId, updateLessonOrderDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Lesson updated successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update lesson: " + e.getMessage())
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
