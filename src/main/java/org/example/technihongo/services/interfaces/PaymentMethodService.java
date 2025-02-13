package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.UpdatePaymentMethodRequestDTO;
import org.example.technihongo.entities.PaymentMethod;

import java.util.List;

public interface PaymentMethodService {
    PaymentMethod updatePaymentMethod(Integer methodId ,UpdatePaymentMethodRequestDTO request);
    List<PaymentMethod> paymentMethodList();
}