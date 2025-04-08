package org.example.technihongo.services.serviceimplements;

import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.*;
import org.example.technihongo.entities.PaymentMethod;
import org.example.technihongo.entities.PaymentTransaction;
import org.example.technihongo.entities.StudentSubscription;
import org.example.technihongo.entities.SubscriptionPlan;
import org.example.technihongo.enums.PaymentMethodCode;
import org.example.technihongo.enums.PaymentMethodType;
import org.example.technihongo.enums.TransactionStatus;
import org.example.technihongo.repositories.PaymentMethodRepository;
import org.example.technihongo.repositories.PaymentTransactionRepository;
import org.example.technihongo.repositories.StudentSubscriptionRepository;
import org.example.technihongo.repositories.SubscriptionPlanRepository;
import org.example.technihongo.services.interfaces.MomoService;
import org.example.technihongo.services.interfaces.StudentSubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class StudentSubscriptionServiceImpl implements StudentSubscriptionService  {

    private static final Logger log = LoggerFactory.getLogger(StudentSubscriptionServiceImpl.class);

    private final StudentSubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final MomoService momoService;
    private final JavaMailSender mailSender;


    @Override
    public RenewSubscriptionResponseDTO initiateRenewal(Integer studentId, RenewSubscriptionRequestDTO request) {
        StudentSubscription currentSubscription = subscriptionRepository
                .findByStudent_StudentIdAndIsActiveTrue(studentId);
        if (currentSubscription == null) {
            throw new RuntimeException("No active subscription found for student ID: " + studentId);
        }
        SubscriptionPlan plan = subscriptionPlanRepository.findById(request.getSubPlanId())
                .orElseThrow(() -> new RuntimeException("Subscription plan not found: " + request.getSubPlanId()));

        PaymentMethod momoMethod = paymentMethodRepository.findByCode(PaymentMethodCode.MOMO_QR);
        if (momoMethod == null || !momoMethod.getName().equals(PaymentMethodType.MomoPay) || !momoMethod.isActive()) {
            throw new IllegalStateException("MoMo payment method is not available or inactive");
        }

        String orderId = "RENEW-" + System.currentTimeMillis();

        PaymentTransaction transaction = PaymentTransaction.builder()
                .subscription(currentSubscription)
                .paymentMethod(momoMethod)
                .transactionAmount(plan.getPrice())
                .transactionStatus(TransactionStatus.PENDING)
                .externalOrderId(orderId)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();
        paymentTransactionRepository.save(transaction);


        String orderInfo = "Gia hạn gói: " + plan.getName();
        CreateMomoResponse momoResponse = momoService.createPaymentQR(orderId, orderInfo, plan.getPrice().longValue());

        return RenewSubscriptionResponseDTO.builder()
                .payUrl(momoResponse.getPayUrl())
                .transactionId(transaction.getTransactionId())
                .build();
    }

    @Override
    public void handleRenewalMoMo(MomoCallbackDTO callback, Map<String, String> requestParams) {
        String orderId = callback.getOrderId();
        log.info("Handling MoMo renewal callback for orderId: {}", orderId);

        PaymentTransaction transaction = paymentTransactionRepository.findByExternalOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Transaction not found for orderId: " + orderId));

        if (transaction.getExpiresAt().isBefore(LocalDateTime.now())) {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            paymentTransactionRepository.save(transaction);
            log.warn("Transaction expired before callback: orderId={}", orderId);
            return;
        }

        if ("0".equals(callback.getResultCode())) {
            transaction.setTransactionStatus(TransactionStatus.COMPLETED);
            transaction.setPaymentDate(LocalDateTime.now());
            paymentTransactionRepository.save(transaction);

            StudentSubscription currentSubscription = transaction.getSubscription();
            log.info("Current subscription found: ID={}, EndDate={}",
                    currentSubscription.getSubscriptionId(),
                    currentSubscription.getEndDate());

            SubscriptionPlan plan = subscriptionPlanRepository.findById(currentSubscription.getSubscriptionPlan().getSubPlanId())
                    .orElseThrow(() -> new RuntimeException("Subscription plan not found"));

            StudentSubscription newSubscription = StudentSubscription.builder()
                    .student(currentSubscription.getStudent())
                    .subscriptionPlan(plan)
                    .startDate(currentSubscription.getEndDate())
                    .endDate(currentSubscription.getEndDate().plusDays(plan.getDurationDays()))
                    .isActive(false)
                    .build();

            StudentSubscription savedSubscription = subscriptionRepository.save(newSubscription);

            log.info("New subscription created: ID={}, StudentID={}, PlanID={}, StartDate={}, EndDate={}, IsActive={}",
                    savedSubscription.getSubscriptionId(),
                    savedSubscription.getStudent().getStudentId(),
                    savedSubscription.getSubscriptionPlan().getSubPlanId(),
                    savedSubscription.getStartDate(),
                    savedSubscription.getEndDate(),
                    savedSubscription.getIsActive());

             transaction.setSubscription(savedSubscription);
             paymentTransactionRepository.save(transaction);

            if (currentSubscription.getEndDate().isBefore(LocalDateTime.now())) {
                currentSubscription.setIsActive(false);
                subscriptionRepository.save(currentSubscription);

                savedSubscription.setIsActive(true);
                subscriptionRepository.save(savedSubscription);

                log.info("Old subscription deactivated, new subscription activated for student: {}",
                        currentSubscription.getStudent().getStudentId());
            } else {
                log.info("New subscription created (pending) for student: {}",
                        currentSubscription.getStudent().getStudentId());
            }
        } else {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            paymentTransactionRepository.save(transaction);
            log.warn("Payment failed or canceled for transaction: {}. ResultCode: {}, Message: {}",
                    transaction.getTransactionId(), callback.getResultCode(), callback.getMessage());
        }
    }

    @Override
    public PageResponseDTO<SubscriptionHistoryDTO> getSubscriptionHistory(Integer studentId, int pageNo, int pageSize, String sortBy, String sortDir) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID must not be null.");
        }

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<StudentSubscription> subscriptions = subscriptionRepository.findAllByStudent_StudentId(studentId, pageable);

        List<SubscriptionHistoryDTO> result = subscriptions.getContent().stream()
                .map(sub -> {
                    List<PaymentTransaction> transactions = paymentTransactionRepository
                            .findAllBySubscription_SubscriptionIdAndTransactionStatusOrderByPaymentDateDesc(sub.getSubscriptionId(), TransactionStatus.COMPLETED);
                    PaymentTransaction latestTransaction = transactions.isEmpty() ? null : transactions.get(0);

                    String groupStatus = determineGroupStatus(sub, latestTransaction);

                    return SubscriptionHistoryDTO.builder()
                            .subscriptionId(sub.getSubscriptionId())
                            .planName(sub.getSubscriptionPlan().getName())
                            .startDate(sub.getStartDate())
                            .endDate(sub.getEndDate())
                            .amount(latestTransaction != null ? latestTransaction.getTransactionAmount() : BigDecimal.ZERO)
                            .paymentMethod(latestTransaction != null ? latestTransaction.getPaymentMethod().getName().name() : "N/A")
                            .status(sub.getIsActive())
                            .groupStatus(groupStatus)
                            .build();
                })
                .collect(Collectors.toList());

        return PageResponseDTO.<SubscriptionHistoryDTO>builder()
                .content(result)
                .pageNo(subscriptions.getNumber())
                .pageSize(subscriptions.getSize())
                .totalElements(subscriptions.getTotalElements())
                .totalPages(subscriptions.getTotalPages())
                .last(subscriptions.isLast())
                .build();
    }

    private String determineGroupStatus(StudentSubscription sub, PaymentTransaction latestTransaction) {
        LocalDateTime now = LocalDateTime.now();
        if (sub.getIsActive()) {
            return "Đang hoạt động";
        } else if (latestTransaction == null) {
            return "Không hoạt động/Hết hạn";
        } else if (sub.getStartDate().isAfter(now)) {
            return "Sắp mở";
        } else {
            return "Không hoạt động/Hết hạn";
        }
    }


    @Override
    @Scheduled(cron = "0 0 9 * * *")
    public void sendExpirationReminders() {
        LocalDateTime threshold = LocalDateTime.now().plusDays(7);
        List<StudentSubscription> expiringSubscriptions = subscriptionRepository.findSubscriptionsExpiringSoon(threshold);

        for (StudentSubscription sub : expiringSubscriptions) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(sub.getStudent().getUser().getEmail());
                helper.setSubject("Thông báo gia hạn gói đăng ký");
                helper.setText(
                        String.format(
                                "Gói %s của bạn sẽ hết hạn vào %s. Nhấn <a href='http://localhost:3000/api/v1/subscription/renew?studentId=%d&subPlanId=%d'>Gia hạn ngay</a>",
                                sub.getSubscriptionPlan().getName(),
                                sub.getEndDate(),
                                sub.getStudent().getStudentId(),
                                sub.getSubscriptionPlan().getSubPlanId()
                        ), true);
                mailSender.send(message);
                log.info("Sent expiration reminder to student: {}", sub.getStudent().getStudentId());
            } catch (Exception e) {
                log.error("Failed to send reminder to student {}: {}", sub.getStudent().getStudentId(), e.getMessage());
            }
        }
    }

    @Scheduled(fixedRate = 86400000)
    @Transactional
    public void activatePendingSubscriptions() {
        List<StudentSubscription> activeSubscriptions = subscriptionRepository.findAllByIsActiveTrue();
        for (StudentSubscription sub : activeSubscriptions) {
            if (sub.getEndDate().isBefore(LocalDateTime.now())) {
                StudentSubscription pendingSub = subscriptionRepository.findByStudent_StudentIdAndIsActiveFalse(sub.getStudent().getStudentId());
                if (pendingSub != null) {
                    sub.setIsActive(false);
                    pendingSub.setIsActive(true);
                    subscriptionRepository.save(sub);
                    subscriptionRepository.save(pendingSub);
                    log.info("Activated pending subscription {} for student {}", pendingSub.getSubscriptionId(), sub.getStudent().getStudentId());
                }
            }
        }
    }
}



