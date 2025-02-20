package org.example.technihongo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "QuizAnswerResponse")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAnswerResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_id")
    private Integer responseId;

    @ManyToOne
    @JoinColumn(name = "attempt_id", nullable = false, referencedColumnName = "attempt_id")
    private StudentQuizAttempt studentQuizAttempt;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "selected_option_id", referencedColumnName = "option_id", nullable = false)
    private QuestionAnswerOption selectedOption;

    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect;
}
