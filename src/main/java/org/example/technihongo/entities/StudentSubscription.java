package org.example.technihongo.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "StudentSubscription")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Integer subscriptionId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false, referencedColumnName = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "sub_plan_id", nullable = false, referencedColumnName = "sub_plan_id")
    private SubscriptionPlan subscriptionPlan;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "is_active")
    private Boolean isActive;
}

