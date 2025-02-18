package org.example.technihongo.repositories;

import org.example.technihongo.entities.PaymentTransaction;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Integer> {
    //for user
//    @Query("SELECT pt FROM PaymentTransaction pt JOIN pt.subscription ss WHERE ss.student.studentId = :studentId")
//    List<PaymentTransaction> findByStudentId(@Param("studentId") Integer studentId);

    List<PaymentTransaction> findBySubscription_Student_StudentId(Integer studentId);

    //for admin
//    @Query("SELECT pt FROM PaymentTransaction pt")
//    List<PaymentTransaction> findAllTransactions();

}



