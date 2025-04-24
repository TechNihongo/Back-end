package org.example.technihongo.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueByPeriodDTO {
    private String period;
    private BigDecimal totalRevenue;
}
