package org.example.technihongo.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MomoCallbackDTO {
    private String partnerCode;
    private String orderId;
    private String requestId;
    private String amount;
    private String resultCode;
    private String message;
    private String signature;
}
