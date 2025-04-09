package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentSubscription;
import org.example.technihongo.entities.SubscriptionPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StudentSubscriptionRepository extends JpaRepository<StudentSubscription, Integer> {
    boolean existsBySubscriptionPlan(SubscriptionPlan subscriptionPlan);
    boolean existsByStudent_StudentIdAndIsActive(Integer studentId, boolean isActive);

    StudentSubscription findByStudent_StudentIdAndIsActiveTrue(Integer studentId);
    StudentSubscription findByStudent_StudentIdAndIsActiveFalse(Integer studentId);
    List<StudentSubscription> findAllByStudent_StudentIdOrderByStartDateDesc(Integer studentId);
    @Query("SELECT s FROM StudentSubscription s WHERE s.isActive = true AND s.endDate <= :threshold")
    List<StudentSubscription> findSubscriptionsExpiringSoon(LocalDateTime threshold);

    List<StudentSubscription> findAllByIsActiveTrue();
    List<StudentSubscription> findAllByStudent_StudentIdAndIsActiveTrue(Integer studentId);
    Page<StudentSubscription> findAllByStudent_StudentId(Integer studentId, Pageable pageable);

    List<StudentSubscription> findByStudentStudentIdAndIsActive(Integer studentId, Boolean isActive);
}
