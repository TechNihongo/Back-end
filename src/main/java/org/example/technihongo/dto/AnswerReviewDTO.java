package org.example.technihongo.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerReviewDTO {
    private Integer questionId;
    private String questionText;
    private Integer selectedOptionId;
    private String selectedOptionText;
    private Boolean isCorrect;
    private String explanation;
}
