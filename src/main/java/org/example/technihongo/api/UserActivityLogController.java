package org.example.technihongo.api;

import jakarta.servlet.http.HttpServletRequest;
import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.UserActivityLogDTO;
import org.example.technihongo.enums.ActivityType;
import org.example.technihongo.enums.ContentType;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.UserActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity-log")
public class UserActivityLogController {
    @Autowired
    private UserActivityLogService userActivityLogService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserActivityLogs(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            List<UserActivityLogDTO> logs = userActivityLogService.getUserActivityLogs(userId, page, size);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Activity logs retrieved successfully")
                    .data(logs)
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

    @GetMapping("/student")
    public ResponseEntity<ApiResponse> getStudentActivityLogs(
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest httpRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                List<UserActivityLogDTO> logs = userActivityLogService.getStudentActivityLogs(userId, page, size);

                String ipAddress = httpRequest.getRemoteAddr();
                String userAgent = httpRequest.getHeader("User-Agent");
                userActivityLogService.trackUserActivityLog(
                        userId,
                        ActivityType.VIEW,
                        ContentType.UserActivityLog,
                        null,
                        ipAddress,
                        userAgent
                );

                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Student activity logs (LOGIN and COMPLETE) retrieved successfully")
                        .data(logs)
                        .build());
            } else {
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
