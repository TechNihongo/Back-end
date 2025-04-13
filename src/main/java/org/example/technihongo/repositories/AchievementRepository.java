package org.example.technihongo.repositories;

import org.example.technihongo.entities.Achievement;
import org.example.technihongo.enums.ConditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Integer> {
    Optional<Achievement> findByBadgeName(String badgeName);
    List<Achievement> findByConditionType(ConditionType conditionType);
}
