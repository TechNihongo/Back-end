package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CourseWithStudyPlanListDTO;
import org.example.technihongo.entities.Lesson;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lesson")
@RequiredArgsConstructor
public class LessonController {
    @Autowired
    private LessonService lessonService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getLessonById(@PathVariable Integer id) throws Exception {
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
    }

    @GetMapping("/csp/{id}")
    public ResponseEntity<ApiResponse> getLessonListByCourseStudyPlanId(@PathVariable Integer id) throws Exception {
        List<Lesson> lessonList = lessonService.getLessonListByCourseStudyPlanId(id);
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
    }
}
