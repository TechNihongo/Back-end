package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionAnswerOptionDTO2 {
    private Integer optionId;
    private String optionText;
    private Boolean isCorrect;
}
