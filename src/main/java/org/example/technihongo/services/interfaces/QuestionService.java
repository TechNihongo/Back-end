package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.QuestionAnswerOptionDTO;
import org.example.technihongo.dto.QuestionWithOptionsDTO;
import org.example.technihongo.dto.CreateUpdateQuestionDTO;
import org.example.technihongo.dto.QuestionWithOptionsResponseDTO;
import org.example.technihongo.entities.Question;
import org.example.technihongo.entities.QuestionAnswerOption;

import java.util.List;

public interface QuestionService {
    List<Question> getQuestionList();
    Question getQuestionById(Integer questionId);
    Question createQuestion(CreateUpdateQuestionDTO createUpdateQuestionDTO);
    void updateQuestion(Integer questionId, CreateUpdateQuestionDTO createUpdateQuestionDTO);
    QuestionWithOptionsResponseDTO createQuestionWithOptions(QuestionWithOptionsDTO dto);
    QuestionWithOptionsResponseDTO updateQuestionWithOptions(Integer questionId, QuestionWithOptionsDTO dto);
    List<QuestionAnswerOption> updateOptions(Integer questionId, List<QuestionAnswerOptionDTO> options);
}
