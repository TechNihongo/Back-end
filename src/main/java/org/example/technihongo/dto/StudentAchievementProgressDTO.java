package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentAchievementProgressDTO {
    private Integer progressId;
    private Integer studentId;
    private Integer achievementId;
    private Integer currentValue;
    private Integer requiredValue;
    private LocalDateTime lastUpdated;
}
