package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.RenewSubscriptionRequestDTO;
import org.example.technihongo.dto.RenewSubscriptionResponseDTO;
import org.example.technihongo.dto.SubscriptionHistoryDTO;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.StudentSubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${spring.application.api-prefix}/subscription")
@RequiredArgsConstructor
public class StudentSubscriptionController {
    private static final Logger log = LoggerFactory.getLogger(StudentSubscriptionController.class);

    private final StudentSubscriptionService subscriptionService;

    @PostMapping("/renew")
    public ResponseEntity<ApiResponse> renewSubscription(@RequestBody RenewSubscriptionRequestDTO request) {
        try {
            RenewSubscriptionResponseDTO response = subscriptionService.initiateRenewal(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Subscription renewal initiated successfully!")
                    .data(response)
                    .build());
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
    @GetMapping("/history")
    public ResponseEntity<ApiResponse> getSubscriptionHistory(@RequestParam("studentId") Integer studentId) {
        try {
            List<SubscriptionHistoryDTO> history = subscriptionService.getSubscriptionHistory(studentId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Subscription history retrieved successfully!")
                    .data(history)
                    .build());
        } catch (RuntimeException e) {
            log.error("Failed to retrieve subscription history: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder()
                    .success(false)
                    .message("Failed to retrieve history: " + e.getMessage())
                    .data(null)
                    .build());
        } catch (Exception e) {
            log.error("Internal error retrieving history: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .success(false)
                    .message("Internal server error: " + e.getMessage())
                    .data(null)
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
