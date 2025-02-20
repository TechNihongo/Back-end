package org.example.technihongo.repositories;

import org.example.technihongo.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    Quiz findByQuizId(Integer quizId);
}
