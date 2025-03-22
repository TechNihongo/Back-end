package org.example.technihongo.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttemptRequestDTO {
    private Integer quizId;
    private Integer attemptId;
    private List<QuizAnswerDTO> answers;
}
