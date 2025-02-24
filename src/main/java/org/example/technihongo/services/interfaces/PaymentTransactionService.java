package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.PaymentHistoryRequestDTO;
import org.example.technihongo.dto.PaymentTransactionDTO;

import java.util.List;

public interface PaymentTransactionService {
    List<PaymentTransactionDTO> getPaymentHistoryByStudentId(Integer studentId);

    List<PaymentTransactionDTO> getAllPaymentHistory(PaymentHistoryRequestDTO requestDTO);
}
