package org.example.technihongo.services.interfaces;

import org.example.technihongo.entities.DifficultyLevel;
import org.example.technihongo.enums.DifficultyLevelEnum;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DifficultyLevelService {
    List<DifficultyLevel> viewAllDifficultyLevels();
    DifficultyLevel viewDifficultyLevelByTag(DifficultyLevelEnum tag);
}
