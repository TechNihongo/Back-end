package org.example.technihongo.api;

import jakarta.servlet.http.HttpServletRequest;
import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.*;
import org.example.technihongo.entities.StudentViolation;
import org.example.technihongo.enums.ActivityType;
import org.example.technihongo.enums.ContentType;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.StudentViolationService;
import org.example.technihongo.services.interfaces.UserActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/violation")
public class StudentViolationController {
    @Autowired
    private StudentViolationService studentViolationService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserActivityLogService userActivityLogService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllStudentViolations(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "violationId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "StudentFlashcardSet") String classifyBy,
            @RequestParam(defaultValue = "") String status){
        try{
            PageResponseDTO<StudentViolation> dto = studentViolationService.getAllStudentViolations(classifyBy, status, pageNo, pageSize, sortBy, sortDir);
            if (dto.getContent().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("List violations is empty!")
                        .build());
            } else {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get Violation List")
                        .data(dto)
                        .build());
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to get violations: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/report")
    public ResponseEntity<ApiResponse> reportViolation(
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest httpRequest,
            @RequestBody ReportViolationRequestDTO request) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer reportedBy = jwtUtil.extractUserId(token);

                StudentViolation violation = studentViolationService.reportViolation(reportedBy, request);

                if(request.getClassifyBy().equalsIgnoreCase("FlashcardSet")) {
                    String ipAddress = httpRequest.getRemoteAddr();
                    String userAgent = httpRequest.getHeader("User-Agent");
                    userActivityLogService.trackUserActivityLog(
                            reportedBy,
                            ActivityType.REPORT_FLASHCARD_SET,
                            ContentType.StudentFlashcardSet,
                            request.getContentId(),
                            ipAddress,
                            userAgent
                    );
                }
                else if(request.getClassifyBy().equalsIgnoreCase("Rating")) {
                    String ipAddress = httpRequest.getRemoteAddr();
                    String userAgent = httpRequest.getHeader("User-Agent");
                    userActivityLogService.trackUserActivityLog(
                            reportedBy,
                            ActivityType.REPORT_RATING,
                            ContentType.StudentCourseRating,
                            request.getContentId(),
                            ipAddress,
                            userAgent
                    );
                }

                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Violation reported successfully")
                        .data(violation)
                        .build());
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Unauthorized")
                                .build());
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Report failed: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/handle/{violationId}")
    public ResponseEntity<ApiResponse> handleViolation(
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest httpRequest,
            @PathVariable Integer violationId,
            @RequestBody HandleViolationRequestDTO request) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer handledBy = jwtUtil.extractUserId(token);

                HandleViolationResponseDTO response = studentViolationService.handleViolation(violationId, handledBy, request);

                String ipAddress = httpRequest.getRemoteAddr();
                String userAgent = httpRequest.getHeader("User-Agent");
                userActivityLogService.trackUserActivityLog(
                        handledBy,
                        ActivityType.HANDLE_REPORT,
                        ContentType.StudentViolation,
                        violationId,
                        ipAddress,
                        userAgent
                );

                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message(response.getMessage())
                        .data(response)
                        .build());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Unauthorized")
                                .build());
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Handle report failed: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse> getViolationSummary(
            @RequestParam String classifyBy,
            @RequestParam(required = false) String status,
            @RequestParam Integer entityId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir){
        try{
            ViolationSummaryDTO dto = studentViolationService.getViolationSummary(
                    classifyBy, status, entityId, pageNo, pageSize, sortBy, sortDir);
            if (dto == null) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("Violation not found!")
                        .build());
            } else if (dto.getDescriptions().getContent().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Violation descriptions are empty!")
                        .build());
            } else {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get Violation List")
                        .data(dto)
                        .build());
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
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
}
