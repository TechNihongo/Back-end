package org.example.technihongo.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.technihongo.enums.DifficultyLevelEnum;
import org.example.technihongo.enums.OccupationStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class UpdateProfileDTO {
    private String userName;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$",
            message = "Password must contain at least 8 characters, including uppercase, lowercase, a digit, and a special character"
    )
    private String password;
    private String confirmPassword;
    private String bio;
    private String profileImg;
    private LocalDate dob;
    private OccupationStatus occupation;
    private boolean reminderEnabled;
    private LocalTime reminderTime;
    private Integer studentId;
    private Integer dailyGoal;
    private DifficultyLevelEnum difficultyLevel;

}
