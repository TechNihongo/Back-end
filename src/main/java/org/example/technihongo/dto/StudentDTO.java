package org.example.technihongo.dto;

import lombok.*;
import org.example.technihongo.enums.DifficultyLevelEnum;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class StudentDTO {
    private Integer studentId;
    private Integer dailyGoal;
    private DifficultyLevelEnum difficultyLevel;

}
