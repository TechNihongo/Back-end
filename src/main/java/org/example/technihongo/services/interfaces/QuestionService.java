package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.CreateUpdateQuestionDTO;
import org.example.technihongo.entities.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getQuestionList();
    Question getQuestionById(Integer questionId);
    Question createQuestion(CreateUpdateQuestionDTO createUpdateQuestionDTO);
    void updateQuestion(Integer questionId, CreateUpdateQuestionDTO createUpdateQuestionDTO);
}
