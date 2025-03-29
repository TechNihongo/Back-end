package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionWithOptionsDTO2 {
    private Integer questionId;
    private String questionType;
    private String questionText;
    private String explanation;
    private String url;
    private List<QuestionAnswerOptionDTO2> options;
}
