package org.example.technihongo.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.MomoCallbackDTO;
import org.example.technihongo.dto.RenewSubscriptionRequestDTO;
import org.example.technihongo.dto.RenewSubscriptionResponseDTO;
import org.example.technihongo.dto.SubscriptionHistoryDTO;
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

import java.util.List;
import java.util.Map;

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

    @GetMapping("/callback/momo")
    public ResponseEntity<ApiResponse> handleMoMoCallback(@RequestParam Map<String, String> request) {
        try {
            log.info("Received MoMo callback: {}", request);

            MomoCallbackDTO callbackDTO = MomoCallbackDTO.builder()
                    .partnerCode(request.get("partnerCode"))
                    .orderId(request.get("orderId"))
                    .requestId(request.get("requestId"))
                    .amount(request.get("amount"))
                    .resultCode(request.get("resultCode"))
                    .message(request.get("message"))
                    .signature(request.get("signature"))
                    .build();

            subscriptionService.handleRenewalMoMo(callbackDTO, request);

            if ("0".equals(callbackDTO.getResultCode())) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Payment completed successfully!")
                        .data(Map.of("orderId", callbackDTO.getOrderId()))
                        .build());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder()
                        .success(false)
                        .message("Payment failed: " + callbackDTO.getMessage())
                        .data(Map.of("orderId", callbackDTO.getOrderId()))
                        .build());
            }
        } catch (Exception e) {
            log.error("Error processing MoMo callback: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .success(false)
                    .message("Error processing callback: " + e.getMessage())
                    .data(null)
                    .build());
        }
    }
    @GetMapping("/history")
    public ResponseEntity<ApiResponse> getSubscriptionHistory(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("studentId") Integer studentId) {
        try {
            Integer authenticatedStudentId = extractStudentId(authorizationHeader);

            if (!authenticatedStudentId.equals(studentId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Unauthorized access to subscription history!")
                                .build());
            }

            List<SubscriptionHistoryDTO> history = subscriptionService.getSubscriptionHistory(studentId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Subscription history retrieved successfully!")
                    .data(history)
                    .build());

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


    private Integer extractStudentId(String authorizationHeader) throws Exception {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            Integer userId = jwtUtil.extractUserId(token);
            return studentService.getStudentIdByUserId(userId);
        }
        throw new Exception("Authorization failed!");
    }
}
