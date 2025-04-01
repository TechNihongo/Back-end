package org.example.technihongo.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)

public class CreateMomoRequest {
    private String partnerCode;
    private String requestType;
    private String ipnUrl;
    private String orderId;
    private String orderInfo;
    private long amount;
    private String requestId;
    private String redirectUrl;
    private String lang;
    private String extraData;
    private String signature;
}
