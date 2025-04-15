package org.example.technihongo.repositories;


import org.example.technihongo.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends  JpaRepository<Student, Integer> {
    Student findByUser_UserId(Integer userId);

    @Query("SELECT s FROM Student s WHERE s.reminderEnabled = true")
    List<Student> findAllWithReminderEnabled();
}
