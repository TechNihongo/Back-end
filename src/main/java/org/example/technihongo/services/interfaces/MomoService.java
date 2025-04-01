package org.example.technihongo.services.interfaces;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.technihongo.dto.CreateMomoRequest;
import org.example.technihongo.dto.CreateMomoResponse;
import org.example.technihongo.dto.MomoCallbackDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MomoService {

    @Value("${payment.momo.partner-code}")
    private String partnerCode;

    @Value("${payment.momo.access-key}")
    private String accessKey;

    @Value("${payment.momo.secret-key}")
    private String secretKey;

    @Value("${payment.momo.return-url}")
    private String redirectUrl;

    @Value("${payment.momo.ipn-url}")
    private String ipnUrl;

    @Value("${payment.momo.request-type}")
    private String requestType;

    @Value("${payment.momo.skip-signature-verification:false}")
    private boolean skipSignatureVerification;

    private final MomoAPI momoAPI;

    public CreateMomoResponse createPaymentQR(String orderId, String orderInfo, long amount) {
        String requestId = UUID.randomUUID().toString();
        String extraData = "";

        String rawSignature = String.format(
                "accessKey=%s&amount=%d&extraData=%s&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
                accessKey, amount, extraData, ipnUrl, orderId, orderInfo, partnerCode, redirectUrl, requestId, requestType);

        String signature;
        try {
            signature = signHmacSHA256(rawSignature, secretKey);
            log.info("Generated signature for request: {}", signature);
        } catch (Exception e) {
            log.error("Error generating signature: {}", e.getMessage());
            throw new RuntimeException("Failed to generate signature", e);
        }

        CreateMomoRequest request = CreateMomoRequest.builder()
                .partnerCode(partnerCode)
                .requestType(requestType)
                .ipnUrl(ipnUrl)
                .redirectUrl(redirectUrl)
                .orderId(orderId)
                .orderInfo(orderInfo)
                .requestId(requestId)
                .extraData(extraData)
                .amount(amount)
                .signature(signature)
                .lang("vi")
                .build();

        try {
            log.info("Sending MoMo request: {}", request);
            CreateMomoResponse response = momoAPI.createMomoQR(request);
            if (response == null || response.getResultCode() != 0) {
                log.error("MoMo API error: {}", response != null ? response.getMessage() : "No response");
                throw new RuntimeException("Failed to create MoMo QR: " + (response != null ? response.getMessage() : "No response"));
            }
            log.info("MoMo QR created: payUrl={}", response.getPayUrl());
            return response;
        } catch (Exception e) {
            log.error("Error calling MoMo API: {}", e.getMessage());
            throw new RuntimeException("Error calling MoMo API", e);
        }
    }

//    public boolean verifyCallbackSignature(MomoCallbackDTO callback, Map<String, String> requestParams) throws Exception {
//        log.info("Callback data: {}", requestParams);
//
//        TreeMap<String, String> sortedParams = new TreeMap<>();
//        sortedParams.put("accessKey", accessKey); // Lấy từ cấu hình
//        sortedParams.put("amount", requestParams.get("amount"));
//        sortedParams.put("message", requestParams.get("message"));
//        sortedParams.put("orderId", requestParams.get("orderId"));
//        sortedParams.put("partnerCode", requestParams.get("partnerCode"));
//        sortedParams.put("requestId", requestParams.get("requestId"));
//        sortedParams.put("responseTime", requestParams.get("responseTime"));
//        sortedParams.put("resultCode", requestParams.get("resultCode"));
//
//        StringBuilder rawSignature = new StringBuilder();
//        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
//            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
//                if (!rawSignature.isEmpty()) {
//                    rawSignature.append("&");
//                }
//                rawSignature.append(entry.getKey()).append("=").append(entry.getValue());
//            }
//        }
//
//        String signature = signHmacSHA256(rawSignature.toString(), secretKey);
//        log.info("Raw signature: {}", rawSignature);
//        log.info("Expected signature: {}", signature);
//        log.info("Received signature: {}", callback.getSignature());
//
//        boolean isValid = signature.equals(callback.getSignature());
//        if (!isValid) {
//            log.warn("Signature verification failed: expected={}, received={}", signature, callback.getSignature());
//        }
//        return isValid;
//    }

    public boolean verifyCallbackSignature(MomoCallbackDTO callback, Map<String, String> requestParams) throws Exception {
        log.info("Callback data: {}", requestParams);

        if (skipSignatureVerification) {
            log.info("Skipping signature verification based on configuration.");
            return true;
        }

        TreeMap<String, String> sortedParams = new TreeMap<>(requestParams);
        sortedParams.remove("signature");

        StringBuilder rawSignature = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            if (!rawSignature.isEmpty()) {
                rawSignature.append("&");
            }
            rawSignature.append(entry.getKey()).append("=").append(entry.getValue() == null ? "" : entry.getValue());
        }

        String signature = signHmacSHA256(rawSignature.toString(), secretKey);
        log.info("Raw signature: {}", rawSignature);
        log.info("Expected signature: {}", signature);
        log.info("Received signature: {}", callback.getSignature());

        boolean isValid = signature.equals(callback.getSignature());
        if (!isValid) {
            log.warn("Signature verification failed: expected={}, received={}", signature, callback.getSignature());
        }
        return isValid;
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