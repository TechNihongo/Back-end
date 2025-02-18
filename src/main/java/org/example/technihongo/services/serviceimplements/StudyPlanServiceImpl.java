package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CreateStudyPlanDTO;
import org.example.technihongo.dto.UpdateStudyPlanDTO;
import org.example.technihongo.entities.CourseStudyPlan;
import org.example.technihongo.entities.StudyPlan;
import org.example.technihongo.repositories.CourseRepository;
import org.example.technihongo.repositories.CourseStudyPlanRepository;
import org.example.technihongo.repositories.StudentStudyPlanRepository;
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
    @Autowired
    private CourseStudyPlanRepository courseStudyPlanRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentStudyPlanRepository studentStudyPlanRepository;

    @Override
    public List<StudyPlan> studyPlanList() {
        return studyPlanRepository.findAll();
    }

    @Override
    public Optional<StudyPlan> getStudyPlan(Integer studyPlanId) {
        return Optional.ofNullable(studyPlanRepository.findByStudyPlanId(studyPlanId));
    }

    @Override
    public List<StudyPlan> getActiveStudyPlansByCourseId(Integer courseId) {
        if(courseRepository.findByCourseId(courseId) == null){
            throw new RuntimeException("Course ID not found!");
        }

        return courseStudyPlanRepository.findByCourse_CourseId(courseId)
                .stream()
                .map(CourseStudyPlan::getStudyPlan)
                .filter(StudyPlan::isActive)
                .toList();
    }

    @Override
    public StudyPlan createStudyPlan(CreateStudyPlanDTO createStudyPlanDTO) {
        StudyPlan studyPlan = studyPlanRepository.save(StudyPlan.builder()
                .title(createStudyPlanDTO.getTitle())
                .description(createStudyPlanDTO.getDescription())
                .hoursPerDay(createStudyPlanDTO.getHours_per_day())
                .totalMonths(createStudyPlanDTO.getTotal_months())
                .build());

        return studyPlan;
    }

    @Override
    public void updateStudyPlan(Integer planId, UpdateStudyPlanDTO updateStudyPlanDTO) {
        StudyPlan studyPlan = studyPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("StudyPlan not found with id: " + planId));

        boolean hasStudents = studentStudyPlanRepository.findAll().stream()
                .anyMatch(s -> s.getCourseStudyPlan().getStudyPlan().getStudyPlanId().equals(planId));

        if (Boolean.FALSE.equals(updateStudyPlanDTO.getIsActive()) && hasStudents) {
            throw new RuntimeException("Cannot deactivate StudyPlan because students are currently enrolled.");
        }

        studyPlan.setTitle(updateStudyPlanDTO.getTitle());
        studyPlan.setDescription(updateStudyPlanDTO.getDescription());
        studyPlan.setHoursPerDay(updateStudyPlanDTO.getHours_per_day());
        studyPlan.setTotalMonths(updateStudyPlanDTO.getTotal_months());
        studyPlan.setActive(updateStudyPlanDTO.getIsActive());

        studyPlanRepository.save(studyPlan);
    }

    @Override
    public void deleteStudyPlan(Integer studyPlanId) {
        StudyPlan studyPlan = studyPlanRepository.findById(studyPlanId)
                .orElseThrow(() -> new RuntimeException("StudyPlan not found with id: " + studyPlanId));

        if (studyPlan.isActive()) {
            throw new RuntimeException("Cannot delete an active StudyPlan.");
        }

        if (courseStudyPlanRepository.existsByStudyPlan(studyPlan)) {
            throw new RuntimeException("Cannot delete StudyPlan because it is linked to a CourseStudyPlan.");
        }

        studyPlanRepository.delete(studyPlan);
    }
}
