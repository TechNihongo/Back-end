package org.example.technihongo.repositories;

import org.example.technihongo.entities.DifficultyLevel;
import org.example.technihongo.enums.DifficultyLevelEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DifficultyLevelRepository extends JpaRepository<DifficultyLevel, Integer> {
    DifficultyLevel findByTag(DifficultyLevelEnum tag);
}
