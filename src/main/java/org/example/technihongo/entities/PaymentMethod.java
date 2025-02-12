package org.example.technihongo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.technihongo.enums.PaymentMethodCode;
import org.example.technihongo.enums.PaymentMethodType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "PaymentMethod")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "method_id")
    private Integer methodId;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 100, nullable = false)
    private PaymentMethodType name;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", length = 50, unique = true, nullable = false)
    private PaymentMethodCode code;

    @Column(name = "is_active")
    private boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
