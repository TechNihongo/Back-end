package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuizQuestionWithNewQuestionDTO {
    private Integer quizId;
    private String questionText;
    private String explanation;
    private String url;
    private List<QuestionAnswerOptionDTO> options;
}
