package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.*;

import java.util.List;

public interface PaymentTransactionService {
    List<PaymentTransactionDTO> getPaymentHistoryByStudentId(Integer studentId);
    List<PaymentTransactionDTO> getAllPaymentHistory(PaymentHistoryRequestDTO requestDTO);
    PaymentResponseDTO initiateMoMoPayment(PaymentRequestDTO requestDTO);
    void handleMoMoCallback(MomoCallbackDTO callbackDTO);
}
