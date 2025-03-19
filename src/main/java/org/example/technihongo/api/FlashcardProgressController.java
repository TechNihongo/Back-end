//package org.example.technihongo.api;
//
//
//import org.example.technihongo.core.security.JwtUtil;
//import org.example.technihongo.dto.FlashcardProgressDTO;
//import org.example.technihongo.dto.FlashcardProgressRequestDTO;
//import org.example.technihongo.response.ApiResponse;
//import org.example.technihongo.services.interfaces.FlashcardProgressService;
//import org.example.technihongo.services.interfaces.StudentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/flashcard-progress")
//public class FlashcardProgressController {
//
//    @Autowired
//    private FlashcardProgressService flashcardProgressService;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private StudentService studentService;
//
//    @GetMapping("/studentSet/{setId}")
//    public ResponseEntity<ApiResponse> getFlashcardProgress(
//            @RequestHeader("Authorization") String authorizationHeader,
//            @PathVariable Integer setId,
//            @RequestParam(defaultValue = "false") boolean isSystemSet) {
//        try {
//            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//                String token = authorizationHeader.substring(7);
//                Integer userId = jwtUtil.extractUserId(token);
//                Integer studentId = studentService.getStudentIdByUserId(userId);
//
//                List<FlashcardProgressDTO> progressList = flashcardProgressService.getFlashcardProgress(studentId, setId, isSystemSet);
//
//                return ResponseEntity.ok(ApiResponse.builder()
//                        .success(true)
//                        .message("Flashcard progress retrieved successfully")
//                        .data(progressList)
//                        .build());
//            } else {
//                throw new Exception("Authorization failed!");
//            }
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(ApiResponse.builder()
//                            .success(false)
//                            .message("Failed to retrieve flashcard progress: " + e.getMessage())
//                            .build());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(ApiResponse.builder()
//                            .success(false)
//                            .message("Internal Server Error: " + e.getMessage())
//                            .build());
//        }
//    }
//
//
//    // starred and is learned
//    @PatchMapping("/updateFlashcardProgress/{flashcardId}")
//    public ResponseEntity<ApiResponse> updateFlashcardProgress(
//            @RequestHeader("Authorization") String authorizationHeader,
//            @PathVariable Integer flashcardId,
//            @RequestBody FlashcardProgressRequestDTO request) {
//        try {
//            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//                String token = authorizationHeader.substring(7);
//                Integer userId = jwtUtil.extractUserId(token);
//                Integer studentId = studentService.getStudentIdByUserId(userId);
//
//                flashcardProgressService.updateFlashcardProgress(studentId, flashcardId,
//                        request.getIsLearned(), request.getStarred());
//
//                return ResponseEntity.ok(ApiResponse.builder()
//                        .success(true)
//                        .message("Flashcard progress updated successfully")
//                        .data(null)
//                        .build());
//            } else {
//                throw new Exception("Authorization failed!");
//            }
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(ApiResponse.builder()
//                            .success(false)
//                            .message("Failed to update flashcard progress: " + e.getMessage())
//                            .build());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(ApiResponse.builder()
//                            .success(false)
//                            .message("Internal Server Error: " + e.getMessage())
//                            .build());
//        }
//    }
//
//
//}
