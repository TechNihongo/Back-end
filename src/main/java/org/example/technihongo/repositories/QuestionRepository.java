package org.example.technihongo.repositories;

import org.example.technihongo.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findByQuestionId(Integer questionId);
}
