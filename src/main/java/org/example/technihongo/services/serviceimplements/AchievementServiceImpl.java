package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.entities.Achievement;
import org.example.technihongo.repositories.AchievementRepository;
import org.example.technihongo.services.interfaces.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AchievementServiceImpl implements AchievementService {
    @Autowired
    private AchievementRepository achievementRepository;
    @Override
    public List<Achievement> achievementList() {
        return achievementRepository.findAll();
    }
}
