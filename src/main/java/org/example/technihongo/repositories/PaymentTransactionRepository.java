package org.example.technihongo.repositories;

import org.example.technihongo.entities.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Integer> {
    List<PaymentTransaction> findBySubscription_Student_StudentId(Integer studentId);
    PaymentTransaction findByTransactionId(Integer transactionId);
    Optional<PaymentTransaction> findByOrderId(String orderId);



}



