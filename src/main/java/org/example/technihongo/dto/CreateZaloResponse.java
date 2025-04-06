package org.example.technihongo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateZaloResponse {
    private int returnCode;
    private String returnMessage;
    private int subReturnCode;
    private String subReturnMessage;
    private String zpTransToken;
    private String orderUrl;
    private String orderToken;
    private String qrCode;
}
