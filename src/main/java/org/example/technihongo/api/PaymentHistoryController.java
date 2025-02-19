package org.example.technihongo.api;

import org.example.technihongo.dto.PaymentHistoryRequestDTO;
import org.example.technihongo.dto.PaymentTransactionDTO;
import org.example.technihongo.enums.TransactionStatus;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.PaymentHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-history")
public class PaymentHistoryController {

    @Autowired
    private PaymentHistoryService paymentHistoryService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse> getPaymentHistoryByStudentId(@PathVariable Integer studentId) {
        try {
            List<PaymentTransactionDTO> history = paymentHistoryService.getPaymentHistoryByStudentId(studentId);
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

            List<PaymentTransactionDTO> history = paymentHistoryService.getAllPaymentHistory(request);
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
