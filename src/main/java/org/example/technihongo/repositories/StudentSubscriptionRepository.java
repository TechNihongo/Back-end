package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentSubscription;
import org.example.technihongo.entities.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentSubscriptionRepository extends JpaRepository<StudentSubscription, Integer> {
    boolean existsBySubscriptionPlan(SubscriptionPlan subscriptionPlan);
}
