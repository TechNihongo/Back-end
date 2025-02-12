package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CourseWithStudyPlanListDTO;
import org.example.technihongo.dto.CreateCourseStudyPlanDTO;
import org.example.technihongo.entities.Course;
import org.example.technihongo.entities.CourseStudyPlan;
import org.example.technihongo.entities.StudyPlan;
import org.example.technihongo.repositories.CourseRepository;
import org.example.technihongo.repositories.CourseStudyPlanRepository;
import org.example.technihongo.repositories.StudyPlanRepository;
import org.example.technihongo.services.interfaces.CourseStudyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Component
public class CourseStudyPlanServiceImpl implements CourseStudyPlanService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudyPlanRepository studyPlanRepository;
    @Autowired
    private CourseStudyPlanRepository courseStudyPlanRepository;

    @Override
    public List<CourseWithStudyPlanListDTO> getCourseListWithStudyPlans() {
        List<Course> courses = courseRepository.findAll();
        List<CourseStudyPlan> courseStudyPlans = courseStudyPlanRepository.findAll();

        return courses.stream().map(course -> {
            List<StudyPlan> studyPlans = courseStudyPlans.stream()
                    .filter(csp -> csp.getCourse().getCourseId().equals(course.getCourseId()))
                    .map(CourseStudyPlan::getStudyPlan)
                    .toList();

            return new CourseWithStudyPlanListDTO(course, studyPlans);
        }).toList();
    }

    @Override
    public Optional<CourseWithStudyPlanListDTO> getCourseWithStudyPlans(Integer courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalCourse.isEmpty()) {
            return Optional.empty();
        }

        Course course = optionalCourse.get();
        List<StudyPlan> studyPlans = courseStudyPlanRepository.findAll().stream()
                .filter(csp -> csp.getCourse().getCourseId().equals(courseId))
                .map(CourseStudyPlan::getStudyPlan)
                .toList();

        return Optional.of(new CourseWithStudyPlanListDTO(course, studyPlans));
    }

    @Override
    public CourseStudyPlan createCourseStudyPlan(CreateCourseStudyPlanDTO createCourseStudyPlanDTO) {
        Course course = courseRepository.findByCourseId(createCourseStudyPlanDTO.getCourseId());
        StudyPlan studyPlan = studyPlanRepository.findByStudyPlanId(createCourseStudyPlanDTO.getStudyPlanId());

        if (courseStudyPlanRepository.existsByCourseAndStudyPlan(course, studyPlan)) {
            throw new IllegalArgumentException("Course already has this study plan!");
        }

        return courseStudyPlanRepository.save(CourseStudyPlan.builder()
                .course(course)
                .studyPlan(studyPlan)
                .build());
    }
}
