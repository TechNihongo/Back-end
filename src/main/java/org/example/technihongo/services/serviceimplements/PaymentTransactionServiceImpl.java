package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.*;
import org.example.technihongo.entities.PaymentTransaction;
import org.example.technihongo.enums.TransactionStatus;
import org.example.technihongo.repositories.*;
import org.example.technihongo.services.interfaces.PaymentTransactionService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class PaymentTransactionServiceImpl implements PaymentTransactionService {
    private final StudentRepository studentRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final StudentSubscriptionRepository studentSubscriptionRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final RestTemplate restTemplate;

    private static final String PARTNER_CODE = "MOMO";
    private static final String ACCESS_KEY = "F8BBA842ECF85";
    private static final String SECRET_KEY = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
    private static final String CREATE_ORDER_URL = "https://test-payment.momo.vn/gw_payment/transactionProcessor";
    private static final String REDIRECT_URL = "http://localhost:8080/api/v1/callback";
    private static final String IPN_URL = "http://localhost:8080/api/v1/ipn";
    private static final String REQUEST_TYPE = "captureMoMoWallet";
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

    @Override
    public PaymentResponseDTO initiateMoMoPayment(PaymentRequestDTO requestDTO) {
        return null;
    }

    @Override
    public void handleMoMoCallback(MomoCallbackDTO callbackDTO) {

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
