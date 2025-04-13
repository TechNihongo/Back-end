package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentAchievementRepository extends JpaRepository<StudentAchievement, Integer> {
    Optional<StudentAchievement> findByStudent_StudentIdAndAchievement_AchievementId(Integer studentId, Integer achievementId);

    List<StudentAchievement> findByStudent_StudentId(Integer studentId);
}
