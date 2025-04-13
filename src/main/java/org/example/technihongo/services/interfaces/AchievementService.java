package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.StudentAchievementDTO;
import org.example.technihongo.entities.Achievement;
import org.example.technihongo.entities.StudentAchievement;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface AchievementService {
    List<Achievement> achievementList();
    void assignAchievementsToStudent(Integer studentId, LocalDateTime loginTime);
    void trackAchievementProgress(Integer studentId);
    StudentAchievementDTO awardAchievement(Integer studentId, Integer achievementId);
    void checkAndAssignStreakAchievements(Integer studentId);
    void checkAndAssignFirstFlashcardAchievement(Integer studentId, Integer studentSetId);
    void checkAndAssignFirstPaymentAchievement(Integer studentId);
    void checkAndAssignFirstFlashcardSetAchievement(Integer studentId);
    void checkAndAssignFlashcardAchievements(Integer studentId);
    void checkAndAssignCourseAchievements(Integer studentId);

    void checkAndAssignFirstFavoriteAchievement(Integer studentId);

    List<StudentAchievement> getStudentAchievements(Integer studentId);
}
