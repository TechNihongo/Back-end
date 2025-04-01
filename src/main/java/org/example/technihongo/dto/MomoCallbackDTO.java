package org.example.technihongo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MomoCallbackDTO {
    private String partnerCode;
    private String orderId;
    private String requestId;
    private String amount;
    private String resultCode;
    private String message;
    private String signature;
    private String responseTime;
    private String orderInfo;
    private String orderType;
    private String payType;
    private String transId;
    private String extraData;
}