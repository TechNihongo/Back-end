package org.example.technihongo.repositories;

import org.example.technihongo.entities.PaymentTransaction;
import org.example.technihongo.enums.TransactionStatus;
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
  

}



