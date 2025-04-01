package org.example.technihongo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {
    private String payUrl;
    private Integer transactionId;
    private String orderId;
    private String qrCodeUrl;
}
