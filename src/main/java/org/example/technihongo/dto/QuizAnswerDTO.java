package org.example.technihongo.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAnswerDTO {
    private Integer questionId;
    private List<Integer> selectedOptionIds;
}
