package org.example.technihongo.dto;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionHistoryDTO {
    private Integer subscriptionId;
    private String planName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal amount;
    private String paymentMethod;
    private Boolean status;
}
