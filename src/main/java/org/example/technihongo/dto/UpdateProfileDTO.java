package org.example.technihongo.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.technihongo.enums.OccupationStatus;

import java.time.LocalTime;

@Getter
@Setter
public class UpdateProfileDTO {
    private String userName;
    private String password;
    private String bio;
    private String profileImg;
    private OccupationStatus occupation;
    private boolean reminderEnabled;
    private LocalTime reminderTime;
}
