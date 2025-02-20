package org.example.technihongo.services.interfaces;

import org.example.technihongo.entities.Quiz;
import java.util.List;

public interface QuizService {
    List<Quiz> getQuizList();
    List<Quiz> getPublicQuizList();
    Quiz getQuizById(Integer quizId);
    Quiz getPublicQuizById(Integer quizId);
}
