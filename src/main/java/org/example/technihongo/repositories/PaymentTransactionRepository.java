package org.example.technihongo.repositories;

import org.example.technihongo.entities.PaymentTransaction;
import org.example.technihongo.enums.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Integer> {
    Optional<PaymentTransaction> findByExternalOrderId(String externalOrderId);
    List<PaymentTransaction> findBySubscription_Student_StudentId(Integer studentId);
    List<PaymentTransaction> findByTransactionStatus(TransactionStatus status);
    List<PaymentTransaction> findBySubscription_Student_StudentIdAndTransactionStatus(Integer studentId, TransactionStatus status);
    PaymentTransaction findByTransactionId(Integer transactionId);
    List<PaymentTransaction> findAllBySubscription_SubscriptionId(Integer subscriptionId);
    Page<PaymentTransaction> findBySubscription_Student_StudentId(
            Integer studentId, Pageable pageable);
    Page<PaymentTransaction> findBySubscription_Student_StudentIdAndTransactionStatus(
            Integer studentId, TransactionStatus transactionStatus, Pageable pageable);


}



