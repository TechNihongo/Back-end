package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.*;
import org.example.technihongo.entities.Quiz;

import java.util.List;

public interface QuizService {
    List<Quiz> getQuizList();
    List<Quiz> getPublicQuizList();
    Quiz getQuizById(Integer quizId);
    Quiz getPublicQuizById(Integer userId, Integer quizId);
    Quiz createQuiz(Integer creatorId, CreateQuizDTO createQuizDTO);
    void updateQuiz(Integer quizId, UpdateQuizDTO updateQuizDTO);
    void updateQuizStatus(Integer quizId, UpdateQuizStatusDTO updateQuizStatusDTO);
    void updateTotalQuestions(Integer quizId);
    List<Quiz> getListQuizzesByCreatorId(Integer creatorId);
}
