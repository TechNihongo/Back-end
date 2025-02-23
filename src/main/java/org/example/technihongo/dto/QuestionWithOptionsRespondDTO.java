package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.technihongo.entities.Question;
import org.example.technihongo.entities.QuestionAnswerOption;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionWithOptionsRespondDTO {
    private Question question;
    private List<QuestionAnswerOption> options;
}
