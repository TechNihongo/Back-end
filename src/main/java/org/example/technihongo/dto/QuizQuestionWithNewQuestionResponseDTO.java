package org.example.technihongo.dto;

import lombok.Builder;
import lombok.Data;
import org.example.technihongo.entities.QuestionAnswerOption;
import org.example.technihongo.entities.QuizQuestion;

import java.util.List;

@Data
@Builder
public class QuizQuestionWithNewQuestionResponseDTO {
    private QuizQuestion quizQuestion;
    private List<QuestionAnswerOption> options;
}
