package org.example.technihongo.repositories;

import org.example.technihongo.entities.PathCourse;
import org.example.technihongo.entities.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Integer> {
    List<QuizQuestion> findByQuiz_QuizId(Integer quizId);
    List<QuizQuestion> findByQuiz_QuizIdOrderByQuestionOrderAsc(Integer quizId);
    int countByQuiz_QuizId(Integer quizId);
}
