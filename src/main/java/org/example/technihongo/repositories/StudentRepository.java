package org.example.technihongo.repositories;


import org.example.technihongo.entities.Student;
import org.example.technihongo.entities.StudentSubscription;
import org.example.technihongo.entities.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends  JpaRepository<Student, Integer> {
    Student findByUser_UserId(Integer userId);


}
