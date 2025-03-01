package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.technihongo.enums.DifficultyLevelEnum;
import org.example.technihongo.enums.OccupationStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {
    private String userName;
    private String bio;
    private String profileImg;
    private Integer dailyGoal;
    private LocalDate dob;
    private DifficultyLevelEnum difficultyLevel;
    private OccupationStatus occupation;
    private LocalTime reminderTime;
}
