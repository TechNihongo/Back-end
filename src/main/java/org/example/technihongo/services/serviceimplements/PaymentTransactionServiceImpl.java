package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.*;
import org.example.technihongo.entities.*;
import org.example.technihongo.enums.PaymentMethodCode;
import org.example.technihongo.enums.PaymentMethodType;
import org.example.technihongo.enums.TransactionStatus;
import org.example.technihongo.repositories.*;
import org.example.technihongo.services.interfaces.MomoService;
import org.example.technihongo.services.interfaces.PaymentTransactionService;
import org.example.technihongo.services.interfaces.ZaloPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentTransactionServiceImpl.class);

    private final StudentRepository studentRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final StudentSubscriptionRepository studentSubscriptionRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final MomoService momoService;
    private final ZaloPayService zaloPayService;

    @Override
    public PageResponseDTO<PaymentTransactionDTO> getPaymentHistoryByStudentId(
            Integer studentId, int pageNo, int pageSize, String sortBy, String sortDir, String transactionStatus) {
        logger.info("Fetching payment history for studentId: {} with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}, transactionStatus: {}",
                studentId, pageNo, pageSize, sortBy, sortDir, transactionStatus);

        if (studentId == null) {
            throw new IllegalArgumentException("Student ID must not be null.");
        }

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<PaymentTransaction> transactions;
        if (transactionStatus != null && !transactionStatus.trim().isEmpty()) {
            try {
                TransactionStatus status = TransactionStatus.valueOf(transactionStatus.toUpperCase());
                transactions = paymentTransactionRepository.findBySubscription_Student_StudentIdAndTransactionStatus(
                        studentId, status, pageable);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid transaction status value. Must be a valid TransactionStatus enum.");
            }
        } else {
            transactions = paymentTransactionRepository.findBySubscription_Student_StudentId(studentId, pageable);
        }

        Page<PaymentTransactionDTO> transactionDTOPage = transactions.map(this::convertToDTO);
        return getPageResponseDTO(transactionDTOPage);
    }

    @Override
    public List<PaymentTransactionDTO> getAllPaymentHistory(PaymentHistoryRequestDTO request) {
        logger.info("Fetching all payment history with request: {}", request);
        List<PaymentTransaction> transactions;

        if (request == null) {
            transactions = paymentTransactionRepository.findAll();
        } else {
            if (request.getStudentId() != null && request.getTransactionStatus() != null) {
                transactions = paymentTransactionRepository.findBySubscription_Student_StudentIdAndTransactionStatus(
                        request.getStudentId(), request.getTransactionStatus());
            } else if (request.getStudentId() != null) {
                transactions = paymentTransactionRepository.findBySubscription_Student_StudentId(request.getStudentId());
            } else if (request.getTransactionStatus() != null) {
                transactions = paymentTransactionRepository.findByTransactionStatus(request.getTransactionStatus());
            } else {
                transactions = paymentTransactionRepository.findAll();
            }
        }

        return transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponseDTO initiateMoMoPayment(Integer studentId, PaymentRequestDTO requestDTO) {
        logger.info("Initiating MoMo payment for studentId: {}, subPlanId: {}", studentId, requestDTO.getSubPlanId());

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        SubscriptionPlan plan = subscriptionPlanRepository.findById(requestDTO.getSubPlanId())
                .orElseThrow(() -> new IllegalArgumentException("Subscription plan not found"));

        StudentSubscription subscription = StudentSubscription.builder()
                .student(student)
                .subscriptionPlan(plan)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(plan.getDurationDays()))
                .isActive(false)
                .build();
        subscription = studentSubscriptionRepository.save(subscription);

        PaymentMethod momoMethod = paymentMethodRepository.findByCode(PaymentMethodCode.MOMO_QR);
        if (momoMethod == null || !momoMethod.getName().equals(PaymentMethodType.MomoPay) || !momoMethod.isActive()) {
            throw new IllegalStateException("MoMo payment method is not available or inactive");
        }

        PaymentTransaction transaction = PaymentTransaction.builder()
                .subscription(subscription)
                .paymentMethod(momoMethod)
                .transactionAmount(plan.getPrice())
                .currency("VND")
                .transactionStatus(TransactionStatus.PENDING)
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();
        transaction = paymentTransactionRepository.save(transaction);

//        String orderId = "TX" + transaction.getTransactionId();
        String orderId = UUID.randomUUID().toString();
        String orderInfo = "Thanh toán SubscriptionPlan: " + plan.getName();
        long amount = plan.getPrice().longValue();
        CreateMomoResponse momoResponse = momoService.createPaymentQR(orderId, orderInfo, amount);

        transaction.setExternalOrderId(orderId);
        paymentTransactionRepository.save(transaction);

        return PaymentResponseDTO.builder()
                .transactionId(transaction.getTransactionId())
                .orderId(orderId)
                .payUrl(momoResponse.getPayUrl())
                .qrCodeUrl(momoResponse.getQrCodeUrl())
                .build();
    }

//    @Override
//    public PaymentResponseDTO initiateZaloPayment(Integer studentId, PaymentRequestDTO requestDTO) {
//        logger.info("Initiating ZaloPay payment for studentId: {}, subPlanId: {}", studentId, requestDTO.getSubPlanId());
//
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
//        SubscriptionPlan plan = subscriptionPlanRepository.findById(requestDTO.getSubPlanId())
//                .orElseThrow(() -> new IllegalArgumentException("Subscription plan not found"));
//
//        StudentSubscription subscription = StudentSubscription.builder()
//                .student(student)
//                .subscriptionPlan(plan)
//                .startDate(LocalDateTime.now())
//                .endDate(LocalDateTime.now().plusDays(plan.getDurationDays()))
//                .isActive(false)
//                .build();
//        subscription = studentSubscriptionRepository.save(subscription);
//
//        PaymentMethod zaloMethod = paymentMethodRepository.findByCode(PaymentMethodCode.ZALOPAY_QR);
//        if (zaloMethod == null || !zaloMethod.getName().equals(PaymentMethodType.ZaloPay) || !zaloMethod.isActive()) {
//            throw new IllegalStateException("ZaloPay payment method is not available or inactive");
//        }
//
//        PaymentTransaction transaction = PaymentTransaction.builder()
//                .subscription(subscription)
//                .paymentMethod(zaloMethod)
//                .transactionAmount(plan.getPrice())
//                .currency("VND")
//                .transactionStatus(TransactionStatus.PENDING)
//                .expiresAt(LocalDateTime.now().plusMinutes(15)) // ZaloPay 15 phút
//                .build();
//        transaction = paymentTransactionRepository.save(transaction);
//
//        String appTransId = new SimpleDateFormat("yyMMdd").format(new Date()) + "_" + transaction.getTransactionId();
//        String orderInfo = "Thanh toán SubscriptionPlan: " + plan.getName();
//        CreateZaloResponse zaloResponse = zaloPayService.createOrder(appTransId, orderInfo, plan.getPrice().longValue(), studentId);
//
//        transaction.setExternalOrderId(appTransId);
//        paymentTransactionRepository.save(transaction);
//
//        return PaymentResponseDTO.builder()
//                .transactionId(transaction.getTransactionId())
//                .orderId(appTransId)
//                .payUrl(zaloResponse.getOrderUrl())
//                .qrCodeUrl(zaloResponse.getQrCode())
//                .build();
//    }

    @Override
    public void handleMoMoCallback(MomoCallbackDTO callbackDTO, Map<String, String> requestParams) {
        logger.info("Handling MoMo callback for orderId: {}", callbackDTO.getOrderId());

        PaymentTransaction transaction = paymentTransactionRepository.findByExternalOrderId(callbackDTO.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found for orderId: " + callbackDTO.getOrderId()));

        if (transaction.getExpiresAt().isBefore(LocalDateTime.now())) {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            paymentTransactionRepository.save(transaction);
            logger.warn("Transaction expired before callback for orderId: {}", callbackDTO.getOrderId());
            return;
        }

        try {
            if (!momoService.verifyCallbackSignature(callbackDTO, requestParams)) {
                transaction.setTransactionStatus(TransactionStatus.FAILED);
                paymentTransactionRepository.save(transaction);
                logger.warn("Invalid signature for transactionId: {}", transaction.getTransactionId());
                throw new SecurityException("Invalid signature from MoMo callback");
            }
        } catch (Exception e) {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            paymentTransactionRepository.save(transaction);
            logger.error("Error verifying signature: {}", e.getMessage());
            throw new RuntimeException("Signature verification failed", e);
        }

        TransactionStatus newStatus;
        try {
            int resultCode = Integer.parseInt(callbackDTO.getResultCode());
            if (resultCode == 0) {
                newStatus = TransactionStatus.COMPLETED;
                transaction.setPaymentDate(LocalDateTime.now());
                transaction.getSubscription().setIsActive(true);
            } else {
                newStatus = TransactionStatus.FAILED;
                logger.warn("Payment failed or canceled. ResultCode: {}, Message: {}",
                        callbackDTO.getResultCode(), callbackDTO.getMessage());
            }
        } catch (NumberFormatException e) {
            newStatus = TransactionStatus.FAILED;
            logger.error("Invalid resultCode format: {}", callbackDTO.getResultCode());
        }

        transaction.setTransactionStatus(newStatus);
        paymentTransactionRepository.save(transaction);

        if (newStatus == TransactionStatus.COMPLETED) {
            studentSubscriptionRepository.save(transaction.getSubscription());
            logger.info("Subscription activated for transactionId: {}", transaction.getTransactionId());
        }

        logger.info("MoMo callback handled for transactionId: {}, new status: {}",
                transaction.getTransactionId(), newStatus);
    }

    @Override
    public PaymentTransaction getPaymentTransactionById(Integer studentId, Integer transactionId) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found!"));

        PaymentTransaction transaction = paymentTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment transaction not found!"));

        if (!Objects.equals(transaction.getSubscription().getStudent().getStudentId(), studentId)) {
            throw new RuntimeException("Student does not have subscription for this transaction!");
        }

        return transaction;
    }

//    @Override
//    public void handleZaloCallback(ZaloPayCallbackDTO callbackDTO, Map<String, String> requestParams) {
//        logger.info("Handling ZaloPay callback for data: {}", callbackDTO.getData());
//
//        Map<String, Object> dataMap;
//        try {
//            dataMap = new com.fasterxml.jackson.databind.ObjectMapper().readValue(callbackDTO.getData(), Map.class);
//        } catch (Exception e) {
//            logger.error("Failed to parse ZaloPay callback data: {}", e.getMessage());
//            throw new RuntimeException("Invalid ZaloPay callback data", e);
//        }
//        String appTransId = (String) dataMap.get("app_trans_id");
//
//        PaymentTransaction transaction = paymentTransactionRepository.findByExternalOrderId(appTransId)
//                .orElseThrow(() -> new IllegalArgumentException("Transaction not found for appTransId: " + appTransId));
//
//        if (transaction.getExpiresAt().isBefore(LocalDateTime.now())) {
//            transaction.setTransactionStatus(TransactionStatus.FAILED);
//            paymentTransactionRepository.save(transaction);
//            logger.warn("ZaloPay transaction expired: appTransId={}", appTransId);
//            return;
//        }
//
//        if (!zaloPayService.verifyCallbackSignature(callbackDTO, requestParams)) {
//            transaction.setTransactionStatus(TransactionStatus.FAILED);
//            paymentTransactionRepository.save(transaction);
//            logger.warn("Invalid ZaloPay signature for transactionId: {}", transaction.getTransactionId());
//            throw new SecurityException("Invalid signature from ZaloPay callback");
//        }
//
//        int status = (int) dataMap.get("status");
//        TransactionStatus newStatus;
//        if (status == 1) {
//            newStatus = TransactionStatus.COMPLETED;
//            transaction.setPaymentDate(LocalDateTime.now());
//            transaction.getSubscription().setIsActive(true);
//        } else {
//            newStatus = TransactionStatus.FAILED;
//            logger.warn("ZaloPay payment failed: status={}", status);
//        }
//
//        transaction.setTransactionStatus(newStatus);
//        paymentTransactionRepository.save(transaction);
//
//        if (newStatus == TransactionStatus.COMPLETED) {
//            studentSubscriptionRepository.save(transaction.getSubscription());
//            logger.info("ZaloPay subscription activated for transactionId: {}", transaction.getTransactionId());
//        }
//
//        logger.info("ZaloPay callback handled: transactionId={}, status={}", transaction.getTransactionId(), newStatus);
//    }

    private PaymentTransactionDTO convertToDTO(PaymentTransaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction is null");
        }

        String subscriptionPlanName = Optional.ofNullable(transaction.getSubscription())
                .map(StudentSubscription::getSubscriptionPlan)
                .map(SubscriptionPlan::getName)
                .orElse("Unknown Plan");

        String paymentMethod = Optional.ofNullable(transaction.getPaymentMethod())
                .map(PaymentMethod::getName)
                .map(PaymentMethodType::toString)
                .orElse("Unknown Method");

        BigDecimal transactionAmount = Optional.ofNullable(transaction.getTransactionAmount())
                .orElse(BigDecimal.ZERO);

        String currency = Optional.ofNullable(transaction.getCurrency())
                .orElse("Unknown Currency");

        TransactionStatus transactionStatus = Optional.ofNullable(transaction.getTransactionStatus())
                .orElse(TransactionStatus.UNKNOWN);

        LocalDateTime paymentDate = transaction.getPaymentDate();
        LocalDateTime createdAt = transaction.getCreatedAt();

        return PaymentTransactionDTO.builder()
                .transactionId(transaction.getTransactionId())
                .subscriptionPlanName(subscriptionPlanName)
                .paymentMethod(PaymentMethodType.valueOf(paymentMethod))
//                .paymentMethodCode(PaymentMethodCode.valueOf(paymentMethodCode))
                .transactionAmount(transactionAmount)
                .currency(currency)
                .transactionStatus(transactionStatus)
                .paymentDate(paymentDate)
                .createdAt(createdAt)
                .build();
    }

    private PageResponseDTO<PaymentTransactionDTO> getPageResponseDTO(Page<PaymentTransactionDTO> page) {
        return PageResponseDTO.<PaymentTransactionDTO>builder()
                .content(page.getContent())
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}