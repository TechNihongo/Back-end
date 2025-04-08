package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentAchievementProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentAchievementProgressRepository extends JpaRepository<StudentAchievementProgress, Integer> {
    Optional<StudentAchievementProgress> findByStudent_StudentIdAndAchievement_AchievementId(Integer studentId, Integer achievementId);
}