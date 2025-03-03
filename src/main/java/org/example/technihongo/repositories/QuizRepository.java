package org.example.technihongo.repositories;

import org.example.technihongo.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    Quiz findByQuizId(Integer quizId);
    List<Quiz> findByCreator_UserId(Integer creatorId);

    boolean existsByDomainDomainId(Integer domainId);

}
