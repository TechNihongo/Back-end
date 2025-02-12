package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.SubscriptionDTO;
import org.example.technihongo.dto.UpdateSubscriptionDTO;
import org.example.technihongo.entities.SubscriptionPlan;

import java.util.List;

public interface SubscriptionPlanService {
    SubscriptionPlan createSubscriptionPlan(SubscriptionDTO subscriptionDTO);
    SubscriptionPlan updateSubscriptionPlan(Integer Id, UpdateSubscriptionDTO updateSubscriptionDTO);
    void deleteSubscriptionPlan(Integer Id);

    List<SubscriptionPlan> subscriptionList();
}
