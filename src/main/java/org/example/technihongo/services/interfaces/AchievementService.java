package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.StudentAchievementDTO;
import org.example.technihongo.entities.Achievement;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface AchievementService {
    List<Achievement> achievementList();
    void assignAchievementsToStudent(Integer studentId, LocalDateTime loginTime);
    void trackAchievementProgress(Integer studentId);
    StudentAchievementDTO awardAchievement(Integer studentId, Integer achievementId);
}
