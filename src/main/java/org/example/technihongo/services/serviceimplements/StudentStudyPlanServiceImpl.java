package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.EnrollStudyPlanRequest;
import org.example.technihongo.dto.StudentStudyPlanDTO;
import org.example.technihongo.dto.StudyPlanDTO;
import org.example.technihongo.dto.SwitchStudyPlanRequestDTO;
import org.example.technihongo.entities.Student;
import org.example.technihongo.entities.StudentStudyPlan;
import org.example.technihongo.entities.StudyPlan;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.repositories.StudentRepository;
import org.example.technihongo.repositories.StudentStudyPlanRepository;
import org.example.technihongo.repositories.StudyPlanRepository;
import org.example.technihongo.services.interfaces.StudentStudyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentStudyPlanServiceImpl implements StudentStudyPlanService {

    @Autowired
    private StudentStudyPlanRepository studentStudyPlanRepository;

    @Autowired
    private StudyPlanRepository studyPlanRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public StudentStudyPlanDTO enrollStudentInStudyPlan(EnrollStudyPlanRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with Id: " + request.getStudentId()));

        StudyPlan studyPlan = studyPlanRepository.findById(request.getStudyPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Study plan not found with ID: " + request.getStudyPlanId()));

        if(!studyPlan.isActive()) {
            throw new ResourceNotFoundException("Study plan is not active!! So that you can't enroll to this StudyPlan!!");
        }

        Optional<StudentStudyPlan> existingPlan = studentStudyPlanRepository.findByStudentIdAndStudyPlanId(
                request.getStudentId(), request.getStudyPlanId());

        if(existingPlan.isPresent() && "Active".equals(existingPlan.get().getStatus())) {
            throw new ResourceNotFoundException("You are already enrolled in this study plan!!");
        }

        Optional<StudentStudyPlan> activePlan = studentStudyPlanRepository.findActiveStudyPlanByStudentId(request.getStudentId());

        StudentStudyPlan newStudentPlan = StudentStudyPlan.builder()
                .student(student)
                .studyPlan(studyPlan)
                .previousPlan(activePlan.orElse(null) != null ? activePlan.get().getStudyPlan() : null)
                .startDate(LocalDateTime.now())
                .status("Active")
                .build();

        if(activePlan.isPresent()) {
            StudentStudyPlan previousPlan = activePlan.get();
            previousPlan.setStatus("Switched");
            previousPlan.setSwitchDate(LocalDateTime.now());
            studentStudyPlanRepository.save(previousPlan);
        }
        StudentStudyPlan savedPlan = studentStudyPlanRepository.save(newStudentPlan);
        return mapToDTO(savedPlan);

    }

    @Override
    public StudentStudyPlanDTO switchStudyPlan(SwitchStudyPlanRequestDTO request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + request.getStudentId()));

        StudyPlan newStudyPlan = studyPlanRepository.findById(request.getNewStudyPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Study plan not found with ID: " + request.getNewStudyPlanId()));

        if (!newStudyPlan.isActive()) {
            throw new IllegalStateException("Cannot switch to an inactive study plan");
        }

        StudentStudyPlan currentPlan = studentStudyPlanRepository.findActiveStudyPlanByStudentId(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("No active study plan found for student with ID: " + request.getStudentId()));

        if (!currentPlan.getStudyPlan().getStudyPlanId().equals(request.getCurrentStudyPlanId())) {
            throw new IllegalStateException("Current study plan ID does not match the active plan");
        }

        StudentStudyPlan newStudentPlan = StudentStudyPlan.builder()
                .student(student)
                .studyPlan(newStudyPlan)
                .previousPlan(currentPlan.getStudyPlan())
                .startDate(LocalDateTime.now())
                .status("Active")
                .build();

        currentPlan.setStatus("Switched");
        currentPlan.setSwitchDate(LocalDateTime.now());
        studentStudyPlanRepository.save(currentPlan);

        StudentStudyPlan savedPlan = studentStudyPlanRepository.save(newStudentPlan);
        return mapToDTO(savedPlan);
    }

    @Override
    public List<StudyPlanDTO> getAvailableStudyPlans(Integer studentId) {
        return studyPlanRepository.findByActiveTrue().stream()
                .map(this::mapToStudyPlanDTO)
                .collect(Collectors.toList());
    }


    //Get the current active study plan for a student
    @Override
    public StudentStudyPlanDTO getActiveStudyPlan(Integer studentId) {
        StudentStudyPlan activePlan = studentStudyPlanRepository.findActiveStudyPlanByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("No active StudyPlan found for student with ID: " + studentId));

        return mapToDTO(activePlan);
    }


    //Get study plan history for a student
    @Override
    public List<StudentStudyPlanDTO> getStudyPlanHistory(Integer studentId) {
        List<StudentStudyPlan> studyPlans = studentStudyPlanRepository.findByStudentStudentId(studentId);
        return studyPlans.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    private StudentStudyPlanDTO mapToDTO(StudentStudyPlan studentStudyPlan) {
        return StudentStudyPlanDTO.builder()
                .studentPlanId(studentStudyPlan.getStudentPlanId())
                .studentId(studentStudyPlan.getStudent().getStudentId())
                .studyPlanId(studentStudyPlan.getStudyPlan().getStudyPlanId())
                .previousPlanId(studentStudyPlan.getPreviousPlan() != null ?
                        studentStudyPlan.getPreviousPlan().getStudyPlanId() : null)
                .startDate(studentStudyPlan.getStartDate())
                .status(studentStudyPlan.getStatus())
                .switchDate(studentStudyPlan.getSwitchDate())
                .build();
    }

    private StudyPlanDTO mapToStudyPlanDTO(StudyPlan studyPlan) {
        return StudyPlanDTO.builder()
                .studyPlanId(studyPlan.getStudyPlanId())
                .courseId(studyPlan.getCourse().getCourseId())
                .title(studyPlan.getTitle())
                .description(studyPlan.getDescription())
                .hoursPerDay(studyPlan.getHoursPerDay())
                .isDefault(studyPlan.isDefault())
                .isActive(studyPlan.isActive())
                .build();
    }
}
