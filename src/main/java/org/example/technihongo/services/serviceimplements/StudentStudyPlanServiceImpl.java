package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.EnrollStudyPlanRequest;
import org.example.technihongo.dto.StudentStudyPlanDTO;
import org.example.technihongo.dto.StudyPlanDTO;
import org.example.technihongo.dto.SwitchStudyPlanRequestDTO;
import org.example.technihongo.entities.*;
import org.example.technihongo.enums.CompletionStatus;
import org.example.technihongo.enums.StudyPlanStatus;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.repositories.*;
import org.example.technihongo.services.interfaces.StudentLessonProgressService;
import org.example.technihongo.services.interfaces.StudentStudyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
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
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private StudentLessonProgressRepository studentLessonProgressRepository;
    @Autowired
    private StudentCourseProgressRepository studentCourseProgressRepository;
    @Autowired
    private StudentLessonProgressService studentLessonProgressService;
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public StudentStudyPlanDTO enrollStudentInStudyPlan(EnrollStudyPlanRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with Id: " + request.getStudentId()));

        StudyPlan studyPlan = studyPlanRepository.findById(request.getStudyPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Study plan not found with ID: " + request.getStudyPlanId()));

        if(!studyPlan.isActive()) {
            throw new ResourceNotFoundException("Study plan không Active!! Nên bạn không thể chuyển qua StudyPlan này được!!");
        }

        Optional<StudentStudyPlan> existingPlan = studentStudyPlanRepository.findByStudentIdAndStudyPlanId(
                request.getStudentId(), request.getStudyPlanId());

        if(existingPlan.isPresent() && existingPlan.get().getStatus().equals(StudyPlanStatus.ACTIVE)) {
            throw new ResourceNotFoundException("Bạn đã truy cập StudyPlan này rồi!!");
        }

        Optional<StudentStudyPlan> activePlan = studentStudyPlanRepository.findActiveStudyPlanByStudentId(request.getStudentId());

        StudentStudyPlan newStudentPlan = StudentStudyPlan.builder()
                .student(student)
                .studyPlan(studyPlan)
                .previousPlan(activePlan.orElse(null) != null ? activePlan.get().getStudyPlan() : null)
                .startDate(LocalDateTime.now())
                .status(StudyPlanStatus.ACTIVE)
                .build();

        if(activePlan.isPresent()) {
            StudentStudyPlan previousPlan = activePlan.get();
            previousPlan.setStatus(StudyPlanStatus.SWITCHED);
            previousPlan.setSwitchDate(LocalDateTime.now());
            studentStudyPlanRepository.save(previousPlan);
        }
        StudentStudyPlan savedPlan = studentStudyPlanRepository.save(newStudentPlan);

        // Tạo StudentLessonProgress cho tất cả Lesson trong StudyPlan
        List<Lesson> lessons = lessonRepository.findByStudyPlan_StudyPlanIdOrderByLessonOrderAsc(studyPlan.getStudyPlanId());
        boolean isDefault = studyPlan.isDefault();
        int lessonOrder = 1;

        for (Lesson lesson : lessons) {
            StudentLessonProgress lessonProgress = StudentLessonProgress.builder()
                    .student(student)
                    .lesson(lesson)
                    .completionPercentage(BigDecimal.ZERO)
                    .completedItems(0)
                    .completionStatus(isDefault ? CompletionStatus.IN_PROGRESS :
                            (lesson.getLessonOrder() == lessonOrder ? CompletionStatus.IN_PROGRESS : CompletionStatus.NOT_STARTED))
                    .build();
            studentLessonProgressRepository.save(lessonProgress);
        }

        return mapToDTO(savedPlan);

    }

    @Override
    public StudentStudyPlanDTO switchStudyPlan(SwitchStudyPlanRequestDTO request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + request.getStudentId()));

        StudyPlan newStudyPlan = studyPlanRepository.findById(request.getNewStudyPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Study plan not found with ID: " + request.getNewStudyPlanId()));

        if (!newStudyPlan.isActive()) {
            throw new IllegalStateException("Không thể chuyển qua StudyPlan bị Inactive được!!");
        }

        StudentStudyPlan currentPlan = studentStudyPlanRepository.findActiveStudyPlanByStudentId(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("No active study plan found for student with ID: " + request.getStudentId()));

        if (!currentPlan.getStudyPlan().getStudyPlanId().equals(request.getCurrentStudyPlanId())) {
            throw new IllegalStateException("Current study plan ID does not match the active plan");
        }

        // Kiểm tra StudentCourseProgress
        StudentCourseProgress courseProgress = studentCourseProgressRepository
                .findByStudent_StudentIdAndCourse_CourseId(request.getStudentId(), currentPlan.getStudyPlan().getCourse().getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course progress not found"));
        if (courseProgress.getCompletionPercentage().compareTo(new BigDecimal("30")) >= 0) {
            throw new IllegalStateException("Không thể đổi StudyPlan nếu tiến độ khóa học đã đạt hoặc hơn 30%!");
        }

        // Chuyển IN_PROGRESS thành PAUSED cho Lesson của StudyPlan cũ
        List<StudentLessonProgress> currentLessons = studentLessonProgressRepository
                .findByStudentStudentIdAndLesson_StudyPlanStudyPlanId(request.getStudentId(), currentPlan.getStudyPlan().getStudyPlanId());
        for (StudentLessonProgress lesson : currentLessons) {
            if (lesson.getCompletionStatus().equals(CompletionStatus.IN_PROGRESS)) {
                lesson.setCompletionStatus(CompletionStatus.PAUSED);
                studentLessonProgressRepository.save(lesson);
            }
        }

        // Tạo StudentStudyPlan mới
        StudentStudyPlan newStudentPlan = StudentStudyPlan.builder()
                .student(student)
                .studyPlan(newStudyPlan)
                .previousPlan(currentPlan.getStudyPlan())
                .startDate(LocalDateTime.now())
                .status(StudyPlanStatus.ACTIVE)
                .build();

        currentPlan.setStatus(StudyPlanStatus.SWITCHED);
        currentPlan.setSwitchDate(LocalDateTime.now());
        studentStudyPlanRepository.save(currentPlan);

        StudentStudyPlan savedPlan = studentStudyPlanRepository.save(newStudentPlan);

        // Chuyển PAUSED thành IN_PROGRESS cho Lesson của StudyPlan mới (nếu đã tồn tại)
        List<StudentLessonProgress> newLessons = studentLessonProgressRepository
                .findByStudentStudentIdAndLesson_StudyPlanStudyPlanId(request.getStudentId(), newStudyPlan.getStudyPlanId());
        if (!newLessons.isEmpty()) {
            for (StudentLessonProgress lesson : newLessons) {
                if (lesson.getCompletionStatus().equals(CompletionStatus.PAUSED)) {
                    lesson.setCompletionStatus(CompletionStatus.IN_PROGRESS);
                    studentLessonProgressRepository.save(lesson);
                }
            }
        } else {
            // Nếu chưa có, tạo mới như enroll
            List<Lesson> lessons = lessonRepository.findByStudyPlan_StudyPlanIdOrderByLessonOrderAsc(newStudyPlan.getStudyPlanId());
            boolean isDefault = newStudyPlan.isDefault();
            int lessonOrder = 1;
            for (Lesson lesson : lessons) {
                StudentLessonProgress lessonProgress = StudentLessonProgress.builder()
                        .student(student)
                        .lesson(lesson)
                        .completionPercentage(BigDecimal.ZERO)
                        .completedItems(0)
                        .completionStatus(isDefault ? CompletionStatus.IN_PROGRESS :
                                (lesson.getLessonOrder() == lessonOrder ? CompletionStatus.IN_PROGRESS : CompletionStatus.NOT_STARTED))
                        .build();
                studentLessonProgressRepository.save(lessonProgress);
            }
        }

        //check progress toàn bộ Lesson
        List<Lesson> lessons = lessonRepository.findByStudyPlan_StudyPlanIdOrderByLessonOrderAsc(newStudyPlan.getStudyPlanId());
        for (Lesson lesson : lessons) {
            studentLessonProgressService.trackStudentLessonProgress(request.getStudentId(), lesson.getLessonId());
        }

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
    public StudentStudyPlanDTO getActiveStudyPlan(Integer studentId, Integer courseId) {
        if (studentId == null || courseId == null) {
            throw new RuntimeException("Student ID and Course ID must not be null.");
        }

        studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

        // Lấy StudentStudyPlan với status là ACTIVE hoặc COMPLETED
        StudentStudyPlan activeOrCompletedPlan = studentStudyPlanRepository
                .findByStudent_StudentIdAndStudyPlan_Course_CourseIdAndStatusIn(
                        studentId, courseId, Arrays.asList(StudyPlanStatus.ACTIVE, StudyPlanStatus.COMPLETED))
                .stream()
                .filter(plan -> plan.getStatus().equals(StudyPlanStatus.ACTIVE)) // Ưu tiên ACTIVE
                .findFirst()
                .orElseGet(() -> studentStudyPlanRepository
                        .findByStudent_StudentIdAndStudyPlan_Course_CourseIdAndStatusIn(
                                studentId, courseId, Arrays.asList(StudyPlanStatus.ACTIVE, StudyPlanStatus.COMPLETED))
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException(
                                "No active or completed StudyPlan found for student with ID: " + studentId +
                                        " in course with ID: " + courseId)));

        return mapToDTO(activeOrCompletedPlan);
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
                .status(String.valueOf(studentStudyPlan.getStatus()))
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
