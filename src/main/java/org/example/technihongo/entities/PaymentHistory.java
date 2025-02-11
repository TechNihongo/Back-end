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
@Table(name = "[StudentAchievementProgress]")
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Integer historyId;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private PaymentTransaction transaction;

    @Column(name = "status")
    private String status;

    @Column(name = "transaction_code")
    private String transactionCode;

    @ManyToOne
    @JoinColumn(name = "admin_confirmed_by")
    private User adminConfirmedBy;

    @Column(name = "admin_confirmed_at")
    private LocalDateTime adminConfirmedAt;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
