package org.example.technihongo.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.*;
import org.example.technihongo.entities.StudentSubscription;
import org.example.technihongo.enums.ActivityType;
import org.example.technihongo.enums.ContentType;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.StudentService;
import org.example.technihongo.services.interfaces.StudentSubscriptionService;
import org.example.technihongo.services.interfaces.UserActivityLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${spring.application.api-prefix}/subscription")
@RequiredArgsConstructor
public class StudentSubscriptionController {
    private static final Logger log = LoggerFactory.getLogger(StudentSubscriptionController.class);

    private final StudentSubscriptionService subscriptionService;
    private final JwtUtil jwtUtil;
    private final StudentService studentService;
    private final UserActivityLogService userActivityLogService;

    @PostMapping("/renew")
    public ResponseEntity<ApiResponse> renewSubscription(
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest httpRequest,
            @RequestBody RenewSubscriptionRequestDTO request) {
        try {
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);
                String ipAddress = httpRequest.getRemoteAddr();
                String userAgent = httpRequest.getHeader("User-Agent");
                userActivityLogService.trackUserActivityLog(
                        userId,
                        ActivityType.RENEW_SUBSCRIPTION,
                        ContentType.StudentSubscription,
                        null,
                        ipAddress,
                        userAgent
                );
                RenewSubscriptionResponseDTO response = subscriptionService.initiateRenewal(studentId, request);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Subscription renewal initiated successfully!")
                        .data(response)
                        .build());
                }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.builder()
                        .success(false)
                        .message("Unauthorized access!")
                        .data(null)
                        .build());
            }

        } catch (RuntimeException e) {
            log.error("Failed to initiate renewal: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder()
                    .success(false)
                    .message("Failed to initiate renewal: " + e.getMessage())
                    .data(null)
                    .build());
        } catch (Exception e) {
            log.error("Internal error during renewal: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .success(false)
                    .message("Internal server error: " + e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @GetMapping("/current-plan")
    public ResponseEntity<ApiResponse> getCurrentSubscriptionPlan(
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest httpRequest) {
        try {
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);
                String ipAddress = httpRequest.getRemoteAddr();
                String userAgent = httpRequest.getHeader("User-Agent");
                userActivityLogService.trackUserActivityLog(
                        userId,
                        ActivityType.RENEW_SUBSCRIPTION,
                        ContentType.StudentSubscription,
                        null,
                        ipAddress,
                        userAgent
                );
                StudentSubscription currentSubscription = subscriptionService.getCurrentSubscriptionByStudentId(studentId);

                if (currentSubscription == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder()
                            .success(false)
                            .message("No active subscription found for student ID: " + studentId)
                            .build());
                }

                SubscriptionPlanDTO subscriptionPlanDTO = SubscriptionPlanDTO.builder()
                        .planName(currentSubscription.getSubscriptionPlan().getName())
                        .startDate(currentSubscription.getStartDate())
                        .endDate(currentSubscription.getEndDate())
                        .isActive(currentSubscription.getIsActive())
                        .build();

                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Current subscription plan retrieved successfully!")
                        .data(subscriptionPlanDTO)
                        .build());
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.builder()
                        .success(false)
                        .message("Unauthorized access!")
                        .data(null)
                        .build());
            }



        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .success(false)
                    .message("Failed to retrieve subscription plan: " + e.getMessage())
                    .build());
        }
    }


    @GetMapping("/history")
    public ResponseEntity<ApiResponse> getSubscriptionHistory(
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest httpRequest,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "startDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);
                String ipAddress = httpRequest.getRemoteAddr();
                String userAgent = httpRequest.getHeader("User-Agent");
                userActivityLogService.trackUserActivityLog(
                        userId,
                        ActivityType.RENEW_SUBSCRIPTION,
                        ContentType.StudentSubscription,
                        null,
                        ipAddress,
                        userAgent
                );
                PageResponseDTO<SubscriptionHistoryDTO> history = subscriptionService.getSubscriptionHistory(studentId, pageNo, pageSize, sortBy, sortDir);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Subscription history retrieved successfully!")
                        .data(history)
                        .build());
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.builder()
                        .success(false)
                        .message("Unauthorized access!")
                        .data(null)
                        .build());
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            log.error("Failed to retrieve subscription history: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve history: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            log.error("Internal error retrieving history: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal server error: " + e.getMessage())
                            .build());
        }
    }
    @PostMapping("/send-reminders")
    public ResponseEntity<ApiResponse> sendExpirationRemindersManually() {
        try {
            subscriptionService.sendExpirationReminders();
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Expiration reminders sent successfully!")
                    .data(null)
                    .build());
        } catch (Exception e) {
            log.error("Failed to send expiration reminders: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .success(false)
                    .message("Failed to send reminders: " + e.getMessage())
                    .data(null)
                    .build());
        }
    }


 
}
