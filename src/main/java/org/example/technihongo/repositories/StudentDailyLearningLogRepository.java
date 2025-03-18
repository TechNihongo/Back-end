package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentDailyLearningLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface StudentDailyLearningLogRepository extends JpaRepository<StudentDailyLearningLog, Integer> {
    Optional<StudentDailyLearningLog> findByStudentStudentIdAndLogDate(Integer studentId, LocalDate logDate);
}
