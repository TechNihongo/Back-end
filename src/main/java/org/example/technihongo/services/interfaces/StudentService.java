package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.StudentDTO;
import org.example.technihongo.dto.UpdateProfileDTO;
import org.example.technihongo.enums.DifficultyLevelEnum;
import org.springframework.transaction.annotation.Transactional;

public interface StudentService {
    StudentDTO setDailyGoal(Integer studentId, Integer dailyGoal);
    StudentDTO updateDifficultyLevel(Integer studentId, DifficultyLevelEnum tag);

    void updateUserName(Integer userId, UpdateProfileDTO dto);
    void updatePassword(Integer userId, UpdateProfileDTO dto);
    @Transactional
    void updateStudentProfile(Integer userId, UpdateProfileDTO dto);
}
