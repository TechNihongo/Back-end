package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.ProfileDTO;
import org.example.technihongo.dto.UpdateProfileDTO;
import org.example.technihongo.enums.DifficultyLevelEnum;
import org.springframework.transaction.annotation.Transactional;

public interface StudentService {
    UpdateProfileDTO setDailyGoal(Integer studentId, Integer dailyGoal);
    UpdateProfileDTO updateDifficultyLevel(Integer studentId, DifficultyLevelEnum tag);

    @Transactional
    void updateStudentProfile(Integer userId, UpdateProfileDTO dto);

    ProfileDTO getStudentProfile(Integer studentId);
    Integer getStudentIdByUserId(Integer userId);
}
