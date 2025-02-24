package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.dto.PaymentHistoryRequestDTO;
import org.example.technihongo.dto.PaymentTransactionDTO;
import org.example.technihongo.entities.PaymentTransaction;
import org.example.technihongo.enums.TransactionStatus;
import org.example.technihongo.repositories.PaymentTransactionRepository;
import org.example.technihongo.services.interfaces.PaymentTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class PaymentTransactionServiceImpl implements PaymentTransactionService {
    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;
    @Override
    public List<PaymentTransactionDTO> getPaymentHistoryByStudentId(Integer studentId) {
        List<PaymentTransaction> transactions = paymentTransactionRepository.findBySubscription_Student_StudentId(studentId);
        return transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentTransactionDTO> getAllPaymentHistory(PaymentHistoryRequestDTO request) {
        List<PaymentTransaction> transactions;

        if (request == null) {
            transactions = paymentTransactionRepository.findAll();
        } else {
            if (request.getStudentId() != null) {
                transactions = paymentTransactionRepository.findBySubscription_Student_StudentId(request.getStudentId());
            } else {
                transactions = paymentTransactionRepository.findAll();
            }
            if (request.getTransactionStatus() != null) {
                transactions = transactions.stream()
                        .filter(t -> Optional.ofNullable(t.getTransactionStatus())
                                .map(status -> status.toString().toUpperCase())
                                .orElse("")
                                .equalsIgnoreCase(request.getTransactionStatus().toString().toUpperCase()))
                        .collect(Collectors.toList());
            }
        }

        return transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



    private PaymentTransactionDTO convertToDTO(PaymentTransaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction is null");
        }

        String subscriptionPlanName = "Unknown Plan";
        if (transaction.getSubscription() != null && transaction.getSubscription().getSubscriptionPlan() != null) {
            subscriptionPlanName = transaction.getSubscription().getSubscriptionPlan().getName();
        }

        String paymentMethod = "Unknown Method";
        if (transaction.getPaymentMethod() != null && transaction.getPaymentMethod().getName() != null) {
            paymentMethod = transaction.getPaymentMethod().getName().toString();
        }

        BigDecimal transactionAmount = transaction.getTransactionAmount() != null
                ? transaction.getTransactionAmount()
                : BigDecimal.ZERO;

        String currency = transaction.getCurrency() != null ? transaction.getCurrency() : "Unknown Currency";

        TransactionStatus transactionStatus = transaction.getTransactionStatus() != null
                ? transaction.getTransactionStatus()
                : TransactionStatus.UNKNOWN;

        LocalDateTime paymentDate = transaction.getPaymentDate() != null ? transaction.getPaymentDate() : LocalDateTime.now();
        LocalDateTime createdAt = transaction.getCreatedAt() != null ? transaction.getCreatedAt() : LocalDateTime.now();

        return PaymentTransactionDTO.builder()
                .transactionId(transaction.getTransactionId())
                .subscriptionPlanName(subscriptionPlanName)
                .paymentMethod(paymentMethod)
                .transactionAmount(transactionAmount)
                .currency(currency)
                .transactionStatus(transactionStatus)
                .paymentDate(paymentDate)
                .createdAt(createdAt)
                .build();
    }


}
