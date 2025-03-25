package org.example.technihongo.repositories;

import org.example.technihongo.entities.QuestionAnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionAnswerOptionRepository extends JpaRepository<QuestionAnswerOption, Integer> {
    QuestionAnswerOption findByOptionId(Integer optionId);
    void deleteByQuestion_QuestionId(Integer questionId);
    List<QuestionAnswerOption> findByQuestion_QuestionId(Integer questionId);
}
