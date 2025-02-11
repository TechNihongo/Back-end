package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.entities.StudyPlan;
import org.example.technihongo.repositories.StudyPlanRepository;
import org.example.technihongo.services.interfaces.StudyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Component
public class StudyPlanServiceImpl implements StudyPlanService {
    @Autowired
    private StudyPlanRepository studyPlanRepository;

    @Override
    public List<StudyPlan> studyPlanList() {
        return studyPlanRepository.findAll();
    }

    @Override
    public Optional<StudyPlan> getStudyPlan(Integer studyPlanId) {
        return Optional.ofNullable(studyPlanRepository.findByStudyPlanId(studyPlanId));
    }
}
