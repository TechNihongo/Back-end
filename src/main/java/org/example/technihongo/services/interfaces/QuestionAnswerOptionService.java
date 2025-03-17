package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.QuestionAnswerOptionListDTO;
import org.example.technihongo.dto.QuestionWithOptionsResponseDTO;
import org.example.technihongo.entities.QuestionAnswerOption;

import java.util.List;

public interface QuestionAnswerOptionService {
    List<QuestionAnswerOption> getOptionListByQuestionId(Integer questionId);
    QuestionAnswerOption getOptionById(Integer optionId);
    QuestionWithOptionsResponseDTO createAnswerOptionList(QuestionAnswerOptionListDTO dto);
    QuestionWithOptionsResponseDTO updateAnswerOptionList(QuestionAnswerOptionListDTO dto);
}
