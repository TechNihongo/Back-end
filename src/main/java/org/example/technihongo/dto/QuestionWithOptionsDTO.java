package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionWithOptionsDTO {
    private String questionText;
    private String explanation;
    private String url;
    private List<QuestionAnswerOptionDTO> options;
}
