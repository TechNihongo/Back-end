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
@Table(name = "[PaymentMethod]")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "method_id")
    private Integer methodId;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "payment_instructions")
    private String paymentInstructions;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
