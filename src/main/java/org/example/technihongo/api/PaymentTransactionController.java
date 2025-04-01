package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.*;
import org.example.technihongo.enums.TransactionStatus;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.PaymentTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.api-prefix}/payment")
public class PaymentTransactionController {

    private final PaymentTransactionService paymentTransactionService;
    private static final Logger log = LoggerFactory.getLogger(PaymentTransactionController.class);

    @PostMapping("/initiateMomo")
    public ResponseEntity<ApiResponse> initiateMoMoPayment(@RequestBody PaymentRequestDTO requestDTO) {
        try {
            PaymentResponseDTO responseDTO = paymentTransactionService.initiateMoMoPayment(requestDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("MoMo payment initiated successfully!")
                    .data(responseDTO)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder()
                    .success(false)
                    .message("Invalid request: " + e.getMessage())
                    .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder()
                    .success(false)
                    .message("Payment method unavailable: " + e.getMessage())
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder()
                    .success(false)
                    .message("Failed to initiate MoMo payment: " + e.getMessage())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .success(false)
                    .message("Internal Server Error: " + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/ipn-handler")
    public ResponseEntity<String> ipnHandler(@RequestParam Map<String, String> request) {
        try {
            MomoCallbackDTO callbackDTO = MomoCallbackDTO.builder()
                    .partnerCode(request.get("partnerCode"))
                    .orderId(request.get("orderId"))
                    .requestId(request.get("requestId"))
                    .amount(request.get("amount"))
                    .resultCode(request.get("resultCode"))
                    .message(request.get("message"))
                    .signature(request.get("signature"))
                    .orderInfo(request.get("orderInfo"))
                    .orderType(request.get("orderType"))
                    .payType(request.get("payType"))
                    .transId(request.get("transId"))
                    .responseTime(request.get("responseTime"))
                    .build();
            paymentTransactionService.handleMoMoCallback(callbackDTO, request);
            return ResponseEntity.ok("Callback processed");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Callback failed: " + e.getMessage());
        }
    }

    @GetMapping("/callback")
    public ResponseEntity<ApiResponse> handleMoMoReturn(@RequestParam Map<String, String> request) {
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
                    .orderInfo(request.get("orderInfo"))
                    .orderType(request.get("orderType"))
                    .payType(request.get("payType"))
                    .transId(request.get("transId"))
                    .responseTime(request.get("responseTime"))
                    .build();

            paymentTransactionService.handleMoMoCallback(callbackDTO, request);
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
                    .build());
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse> getPaymentHistoryByStudentId(@PathVariable Integer studentId) {
        try {
            List<PaymentTransactionDTO> history = paymentTransactionService.getPaymentHistoryByStudentId(studentId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Payment history retrieved successfully!")
                    .data(history)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder()
                    .success(false)
                    .message("Failed to retrieve payment history: " + e.getMessage())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .success(false)
                    .message("Internal Server Error: " + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllPaymentHistory(
            @RequestParam(required = false) Integer studentId,
            @RequestParam(required = false) String transactionStatus) {
        try {
            PaymentHistoryRequestDTO request = new PaymentHistoryRequestDTO();
            request.setStudentId(studentId);
            if (transactionStatus != null) {
                request.setTransactionStatus(TransactionStatus.valueOf(transactionStatus.toUpperCase()));
            }

            List<PaymentTransactionDTO> history = paymentTransactionService.getAllPaymentHistory(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Payment history retrieved successfully!")
                    .data(history)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder()
                    .success(false)
                    .message("Invalid transaction status: " + e.getMessage())
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder()
                    .success(false)
                    .message("Failed to retrieve payment history: " + e.getMessage())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .success(false)
                    .message("Internal Server Error: " + e.getMessage())
                    .build());
        }
    }
}