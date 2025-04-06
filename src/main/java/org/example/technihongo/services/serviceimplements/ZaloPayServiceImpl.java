package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.technihongo.dto.CreateZaloRequest;
import org.example.technihongo.dto.CreateZaloResponse;
import org.example.technihongo.dto.ZaloPayCallbackDTO;
import org.example.technihongo.services.interfaces.ZaloPayAPI;
import org.example.technihongo.services.interfaces.ZaloPayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ZaloPayServiceImpl implements ZaloPayService {

    @Value("${payment.zaloPay.app-id}")
    private int appId;

    @Value("${payment.zaloPay.key1}")
    private String key1;

    @Value("${payment.zaloPay.key2}")
    private String key2;

    @Value("${payment.zaloPay.return-url}")
    private String returnUrl;

    private final ZaloPayAPI zaloPayAPI;

    @Override
    public CreateZaloResponse createOrder(String appTransId, String orderInfo, long amount, Integer studentId) {
        String appUser = "student_" + studentId;
        long appTime = System.currentTimeMillis();
        String embedData = "{\"studentId\": " + studentId + "}";
        String item = "[{\"name\": \"" + orderInfo + "\", \"amount\": " + amount + "}]";

        String rawSignature = appId + "|" + appTransId + "|" + appUser + "|" + amount + "|" + appTime + "|" + embedData + "|" + item;
        String mac;
        try {
            mac = signHmacSHA256(rawSignature, key1);
            log.info("Generated ZaloPay signature: {}", mac);
        } catch (Exception e) {
            log.error("Error generating ZaloPay signature: {}", e.getMessage());
            throw new RuntimeException("Failed to generate ZaloPay signature", e);
        }

        CreateZaloRequest request = CreateZaloRequest.builder()
                .appId(appId)
                .appUser(appUser)
                .appTime(appTime)
                .amount(amount)
                .appTransId(appTransId)
                .bankCode("zalopayapp")
                .embedData(embedData)
                .item(item)
                .callbackUrl(returnUrl)
                .description(orderInfo)
                .mac(mac)
                .build();

        try {
            log.info("Sending ZaloPay request: {}", request);
            CreateZaloResponse response = zaloPayAPI.createZaloOrder(request);
            if (response.getReturnCode() != 1) {
                log.error("ZaloPay API error: {}", response.getReturnMessage());
                throw new RuntimeException("Failed to create ZaloPay order: " + response.getReturnMessage());
            }
            log.info("ZaloPay order created: orderUrl={}", response.getOrderUrl());
            return response;
        } catch (Exception e) {
            log.error("Error calling ZaloPay API: {}", e.getMessage());
            throw new RuntimeException("Error calling ZaloPay API", e);
        }
    }

    @Override
    public boolean verifyCallbackSignature(ZaloPayCallbackDTO callback, Map<String, String> requestParams) {
        try {
            String data = callback.getData();
            String mac = signHmacSHA256(data, key2);
            boolean isValid = mac.equals(callback.getMac());
            if (!isValid) {
                log.warn("ZaloPay signature verification failed: expected={}, received={}", mac, callback.getMac());
            } else {
                log.info("ZaloPay signature verified successfully");
            }
            return isValid;
        } catch (Exception e) {
            log.error("Error verifying ZaloPay callback signature: {}", e.getMessage());
            return false;
        }
    }

    private String signHmacSHA256(String data, String key) throws Exception {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSHA256.init(secretKeySpec);
        byte[] hash = hmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
