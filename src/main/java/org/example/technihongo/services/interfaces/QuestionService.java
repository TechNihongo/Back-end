package org.example.technihongo.services.interfaces;

import org.example.technihongo.entities.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getQuestionList();
    Question getQuestionById(Integer questionId);
}
