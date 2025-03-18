package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentLearningStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentLearningStatisticsRepository extends JpaRepository<StudentLearningStatistics, Integer> {
    Optional<StudentLearningStatistics> findByStudentStudentId(Integer studentId);
}
