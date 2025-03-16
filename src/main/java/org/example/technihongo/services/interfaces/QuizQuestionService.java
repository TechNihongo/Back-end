package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.CreatePathCourseDTO;
import org.example.technihongo.dto.CreateQuizQuestionDTO;
import org.example.technihongo.dto.UpdatePathCourseOrderDTO;
import org.example.technihongo.dto.UpdateQuizQuestionOrderDTO;
import org.example.technihongo.entities.QuizQuestion;

import java.util.List;

public interface QuizQuestionService {
    List<QuizQuestion> getQuizQuestionsByQuizId(Integer quizId);
    QuizQuestion getQuizQuestionById(Integer quizQuestionId);
    QuizQuestion createQuizQuestion(CreateQuizQuestionDTO createQuizQuestionDTO);
    void updateQuizQuestionOrder(Integer quizId, UpdateQuizQuestionOrderDTO updateQuizQuestionOrderDTO);
    void deleteQuizQuestion(Integer quizQuestionId);
    void setQuizQuestionOrder(Integer quizId, Integer quizQuestionId, Integer newOrder);
}
