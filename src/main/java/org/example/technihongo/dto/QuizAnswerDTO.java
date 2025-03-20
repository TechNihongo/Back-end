package org.example.technihongo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAnswerDTO {
    private Integer questionId;
    private Integer selectedOptionId;
}
