package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.QuestionAnswerOptionListDTO;
import org.example.technihongo.dto.QuestionWithOptionsRespondDTO;
import org.example.technihongo.entities.QuestionAnswerOption;

import java.util.List;

public interface QuestionAnswerOptionService {
    List<QuestionAnswerOption> getOptionListByQuestionId(Integer questionId);
    QuestionAnswerOption getOptionById(Integer optionId);
    QuestionWithOptionsRespondDTO createAnswerOptionList(QuestionAnswerOptionListDTO dto);
    QuestionWithOptionsRespondDTO updateAnswerOptionList(QuestionAnswerOptionListDTO dto);
}
