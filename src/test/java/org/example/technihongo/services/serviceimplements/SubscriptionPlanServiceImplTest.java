package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.entities.SubscriptionPlan;
import org.example.technihongo.repositories.SubscriptionPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SubscriptionPlanServiceImplTest {

    @Mock
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @InjectMocks
    private SubscriptionPlanServiceImpl subscriptionPlanService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSubscriptionPlanByIdReturnsPlanWhenIdExists() {
        Integer planId = 1;
        SubscriptionPlan plan = SubscriptionPlan.builder()
                .subPlanId(planId)
                .name("Premium")
                .price(new BigDecimal("49.99"))
                .benefits("All features")
                .durationDays(30)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        when(subscriptionPlanRepository.findById(planId)).thenReturn(Optional.of(plan));

        SubscriptionPlan result = subscriptionPlanService.getSubscriptionPlanById(planId);

        assertNotNull(result);
        assertEquals(planId, result.getSubPlanId());
        assertEquals("Premium", result.getName());
    }

    @Test
    void testGetSubscriptionPlanByIdPopulatesAllFields() {
        Integer planId = 2;
        LocalDateTime now = LocalDateTime.now();
        SubscriptionPlan plan = SubscriptionPlan.builder()
                .subPlanId(planId)
                .name("Standard")
                .price(new BigDecimal("19.99"))
                .benefits("Basic features")
                .durationDays(15)
                .isActive(false)
                .createdAt(now)
                .build();

        when(subscriptionPlanRepository.findById(planId)).thenReturn(Optional.of(plan));

        SubscriptionPlan result = subscriptionPlanService.getSubscriptionPlanById(planId);

        assertEquals(planId, result.getSubPlanId());
        assertEquals("Standard", result.getName());
        assertEquals(new BigDecimal("19.99"), result.getPrice());
        assertEquals("Basic features", result.getBenefits());
        assertEquals(15, result.getDurationDays());
        assertFalse(result.isActive());
        assertEquals(now, result.getCreatedAt());
    }

    @Test
    void testGetSubscriptionPlanByIdWithMultiplePlans() {
        Integer planId1 = 3;
        Integer planId2 = 4;
        SubscriptionPlan plan1 = SubscriptionPlan.builder()
                .subPlanId(planId1)
                .name("Basic")
                .price(new BigDecimal("9.99"))
                .benefits("Limited features")
                .durationDays(7)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        SubscriptionPlan plan2 = SubscriptionPlan.builder()
                .subPlanId(planId2)
                .name("Pro")
                .price(new BigDecimal("99.99"))
                .benefits("All features + support")
                .durationDays(90)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        when(subscriptionPlanRepository.findById(planId2)).thenReturn(Optional.of(plan2));

        SubscriptionPlan result = subscriptionPlanService.getSubscriptionPlanById(planId2);

        assertNotNull(result);
        assertEquals(planId2, result.getSubPlanId());
        assertEquals("Pro", result.getName());
    }

    @Test
    void testGetSubscriptionPlanByIdThrowsExceptionForNonExistentId() {
        Integer nonExistentId = 999;
        when(subscriptionPlanRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                subscriptionPlanService.getSubscriptionPlanById(nonExistentId)
        );
        assertEquals("Subscription Plan not found!", exception.getMessage());
    }

    @Test
    void testGetSubscriptionPlanByIdThrowsExceptionForNullId() {
        when(subscriptionPlanRepository.findById(null)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                subscriptionPlanService.getSubscriptionPlanById(null)
        );
        assertEquals("Subscription Plan not found!", exception.getMessage());
    }

    @Test
    void testGetSubscriptionPlanByIdHandlesRepositoryException() {
        Integer planId = 5;
        when(subscriptionPlanRepository.findById(planId)).thenThrow(new IllegalStateException("DB error"));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                subscriptionPlanService.getSubscriptionPlanById(planId)
        );
        assertEquals("DB error", exception.getMessage());
    }
}