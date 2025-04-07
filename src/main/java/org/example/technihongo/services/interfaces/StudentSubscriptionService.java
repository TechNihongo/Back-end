package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.MomoCallbackDTO;
import org.example.technihongo.dto.RenewSubscriptionRequestDTO;
import org.example.technihongo.dto.RenewSubscriptionResponseDTO;
import org.example.technihongo.dto.SubscriptionHistoryDTO;

import java.util.List;
import java.util.Map;

public interface StudentSubscriptionService {
    RenewSubscriptionResponseDTO initiateRenewal(Integer studentId, RenewSubscriptionRequestDTO request);
    void handleRenewalMoMo(MomoCallbackDTO callback, Map<String, String> requestParams);
    List<SubscriptionHistoryDTO> getSubscriptionHistory(Integer studentId);
    void sendExpirationReminders();

}
