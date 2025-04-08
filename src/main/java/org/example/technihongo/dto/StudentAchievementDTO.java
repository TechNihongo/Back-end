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
public class StudentAchievementDTO {
    private Integer studentAchievementId;
    private Integer studentId;
    private Integer achievementId;
    private LocalDateTime achievedAt;
}
