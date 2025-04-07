package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.*;
import org.example.technihongo.entities.PaymentTransaction;

import java.util.List;
import java.util.Map;

public interface PaymentTransactionService {
    PageResponseDTO<PaymentTransactionDTO> getPaymentHistoryByStudentId(Integer studentId, int pageNo, int pageSize, String sortBy, String sortDir, String transactionStatus);
    List<PaymentTransactionDTO> getAllPaymentHistory(PaymentHistoryRequestDTO requestDTO);
    PaymentResponseDTO initiateMoMoPayment(Integer studentId, PaymentRequestDTO requestDTO);

    void handleMoMoCallback(MomoCallbackDTO callbackDTO, Map<String, String> requestParams);
    PaymentTransaction getPaymentTransactionById(Integer studentId, Integer transactionId);
}
