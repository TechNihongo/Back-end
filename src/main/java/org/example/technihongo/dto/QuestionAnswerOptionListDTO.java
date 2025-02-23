package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnswerOptionListDTO {
    private Integer questionId;
    private List<QuestionAnswerOptionDTO> options;
}
