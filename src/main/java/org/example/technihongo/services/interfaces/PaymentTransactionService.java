package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface PaymentTransactionService {
    List<PaymentTransactionDTO> getPaymentHistoryByStudentId(Integer studentId);
    List<PaymentTransactionDTO> getAllPaymentHistory(PaymentHistoryRequestDTO requestDTO);
    PaymentResponseDTO initiateMoMoPayment(PaymentRequestDTO requestDTO);

    @Transactional
    void handleMoMoCallback(MomoCallbackDTO callbackDTO, Map<String, String> requestParams);
}
