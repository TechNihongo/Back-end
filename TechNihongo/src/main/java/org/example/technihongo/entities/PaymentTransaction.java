package org.example.technihongo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "[PaymentTransaction]")
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transactionId;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private StudentSubscription subscription;

    @ManyToOne
    @JoinColumn(name = "method_id")
    private PaymentMethod paymentMethod;

    @Column(name = "final_amount", precision = 10, scale = 2)
    private Integer finalAmount;

    @Column(name = "currency")
    @Builder.Default
    private String currency = "VND";

    @Column(name = "payment_instructions")
    private String paymentInstructions;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();


    
    
}
