package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentQuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentQuizAttemptRepository extends JpaRepository<StudentQuizAttempt, Integer> {
    List<StudentQuizAttempt> findByStudentStudentIdAndQuizQuizId(Integer studentId, Integer quizId);
    Integer countByStudentStudentIdAndQuizQuizIdAndIsPassedAndIsCompleted
            (Integer studentId, Integer quizId, boolean isPassed, boolean isCompleted);
    boolean existsByStudentStudentIdAndQuizQuizIdAndIsPassedAndIsCompleted
            (Integer studentId, Integer quizId, boolean isPassed, boolean isCompleted);
}
