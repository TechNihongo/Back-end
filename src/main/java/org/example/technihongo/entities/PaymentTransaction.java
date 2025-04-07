package org.example.technihongo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.technihongo.enums.TransactionStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PaymentTransaction")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transactionId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "subscription_id", nullable = false, referencedColumnName = "subscription_id")
    private StudentSubscription subscription;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "method_id", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "external_order_id")
    private String externalOrderId;

    @Column(name = "transaction_amount", precision = 10, scale = 2)
    private BigDecimal transactionAmount;

    @Column(name = "currency", length = 3)
    @Builder.Default
    private String currency = "VND";

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", length = 50)
    private TransactionStatus transactionStatus;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
}


