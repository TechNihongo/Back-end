package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.CreateZaloResponse;
import org.example.technihongo.dto.ZaloPayCallbackDTO;

import java.util.Map;

public interface ZaloPayService {
    CreateZaloResponse createOrder(String appTransId, String orderInfo, long amount, Integer studentId);
    boolean verifyCallbackSignature(ZaloPayCallbackDTO callback, Map<String, String> requestParams);
}