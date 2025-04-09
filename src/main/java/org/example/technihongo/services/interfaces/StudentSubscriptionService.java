package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.*;
import org.example.technihongo.entities.StudentSubscription;

import java.util.Map;

public interface StudentSubscriptionService {
    RenewSubscriptionResponseDTO initiateRenewal(Integer studentId, RenewSubscriptionRequestDTO request);
    void handleRenewalMoMo(MomoCallbackDTO callback, Map<String, String> requestParams);
    PageResponseDTO<SubscriptionHistoryDTO> getSubscriptionHistory(
            Integer studentId, int pageNo, int pageSize, String sortBy, String sortDir);
    StudentSubscription getCurrentSubscriptionByStudentId(Integer studentId);

    void sendExpirationReminders();
}
