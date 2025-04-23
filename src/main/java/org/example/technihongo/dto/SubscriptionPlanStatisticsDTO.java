package org.example.technihongo.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionPlanStatisticsDTO {
    private Integer subPlanId;
    private String planName;
    private Long purchaseCount;
    private BigDecimal totalRevenue;
}
