package org.example.technihongo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateZaloRequest {
    private int appId;
    private String appUser;
    private long appTime;
    private long amount;
    private String appTransId;
    private String bankCode;
    private String embedData;
    private String item;
    private String callbackUrl;
    private String description;
    private String mac;
}
