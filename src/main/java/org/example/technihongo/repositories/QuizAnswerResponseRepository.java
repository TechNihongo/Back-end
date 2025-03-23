package org.example.technihongo.repositories;

import org.example.technihongo.entities.QuizAnswerResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizAnswerResponseRepository extends JpaRepository<QuizAnswerResponse, Integer> {
    List<QuizAnswerResponse> findByStudentQuizAttempt_AttemptId(Integer attemptId);
}
