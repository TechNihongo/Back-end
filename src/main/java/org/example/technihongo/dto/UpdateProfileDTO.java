package org.example.technihongo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.technihongo.enums.DifficultyLevelEnum;
import org.example.technihongo.enums.OccupationStatus;

import java.time.LocalTime;

@Getter
@Setter
@Builder
public class UpdateProfileDTO {
    private String userName;
    private String password;
    private String confirmPassword;
    private String bio;
    private String profileImg;
    private OccupationStatus occupation;
    private boolean reminderEnabled;
    private LocalTime reminderTime;
    private Integer studentId;
    private Integer dailyGoal;
    private DifficultyLevelEnum difficultyLevel;

}
