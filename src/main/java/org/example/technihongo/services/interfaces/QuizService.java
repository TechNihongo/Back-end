package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.CreateQuizDTO;
import org.example.technihongo.dto.UpdateQuizDTO;
import org.example.technihongo.dto.UpdateQuizStatusDTO;
import org.example.technihongo.entities.Quiz;
import java.util.List;

public interface QuizService {
    List<Quiz> getQuizList();
    List<Quiz> getPublicQuizList();
    Quiz getQuizById(Integer quizId);
    Quiz getPublicQuizById(Integer quizId);
    Quiz createQuiz(Integer creatorId, CreateQuizDTO createQuizDTO);
    void updateQuiz(Integer quizId, UpdateQuizDTO updateQuizDTO);
    void updateQuizStatus(Integer quizId, UpdateQuizStatusDTO updateQuizStatusDTO);
    void updateTotalQuestions(Integer quizId);
}
