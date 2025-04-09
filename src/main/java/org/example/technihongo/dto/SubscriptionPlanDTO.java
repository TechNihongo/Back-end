package org.example.technihongo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlanDTO {
    private String planName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isActive;
}
