package org.example.technihongo.services.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import org.example.technihongo.dto.PaymentRequestDTO;
import org.example.technihongo.dto.PaymentResponseDTO;
import org.example.technihongo.dto.RenewSubscriptionRequestDTO;
import org.example.technihongo.dto.RenewSubscriptionResponseDTO;

import java.util.Map;

public interface VNPayService {
    PaymentResponseDTO initiateVNPayPayment(Integer studentId, PaymentRequestDTO requestDTO, HttpServletRequest request);
    void handleVNPayCallback(Map<String, String> requestParams);
    RenewSubscriptionResponseDTO initiateRenewalVNPay(Integer studentId, RenewSubscriptionRequestDTO requestDTO, HttpServletRequest request);
    void handleRenewalVNPay(Map<String, String> requestParams);
}