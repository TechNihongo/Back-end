package org.example.technihongo.dto;


import lombok.Getter;
import lombok.Setter;
import org.example.technihongo.enums.PaymentMethodType;

@Getter
@Setter
public class UpdatePaymentMethodRequestDTO {
    private Integer methodId;
    private PaymentMethodType name;
    private boolean isActive = false;
}
