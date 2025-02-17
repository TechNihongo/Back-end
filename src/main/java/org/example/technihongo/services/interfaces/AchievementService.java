package org.example.technihongo.services.interfaces;

import org.example.technihongo.entities.Achievement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AchievementService {
    List<Achievement> achievementList();
}
