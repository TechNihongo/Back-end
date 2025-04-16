package org.example.technihongo.repositories;

import org.example.technihongo.entities.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Integer> {
    boolean existsByName(String name);

    @Query("SELECT p FROM SubscriptionPlan p WHERE p.price = :price AND p.isActive = true")
    List<SubscriptionPlan> findByPriceAndIsActiveTrue(@Param("price") BigDecimal price);
}
