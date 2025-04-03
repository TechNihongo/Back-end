package org.example.technihongo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RenewSubscriptionResponseDTO {
    private String payUrl;
    private Integer transactionId;
}
