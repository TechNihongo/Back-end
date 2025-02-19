package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CoursePublicDTO;
import org.example.technihongo.dto.CreateCourseDTO;
import org.example.technihongo.dto.UpdateCourseDTO;
import org.example.technihongo.entities.Course;
import org.example.technihongo.entities.Domain;
import org.example.technihongo.repositories.*;
import org.example.technihongo.services.interfaces.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Component
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DomainRepository domainRepository;
    @Autowired
    private DifficultyLevelRepository difficultyLevelRepository;
    @Autowired
    private StudentStudyPlanRepository studentStudyPlanRepository;

    @Override
    public List<Course> courseList() {
        return courseRepository.findAll();
    }

    @Override
    public List<CoursePublicDTO> getPublicCourses() {
        List<Course> courseList = courseRepository.findAll();

        return courseList.stream()
                .filter(Course::isPublic)
                .map(course -> new CoursePublicDTO(course.getCourseId(), course.getTitle(),
                        course.getDescription(), course.getDomain(), course.getDifficultyLevel(),
                        course.getAttachmentUrl(), course.getThumbnailUrl(), course.getEstimatedDuration()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Course> getCourseById(Integer courseId) {
        return Optional.ofNullable(courseRepository.findByCourseId(courseId));
    }

    @Override
    public Optional<CoursePublicDTO> getPublicCourseById(Integer courseId) {
        List<Course> courseList = courseRepository.findAll();

        return courseList.stream()
                .filter(course -> course.isPublic() && course.getCourseId().equals(courseId))
                .map(course -> new CoursePublicDTO(course.getCourseId(), course.getTitle(),
                        course.getDescription(), course.getDomain(), course.getDifficultyLevel(),
                        course.getAttachmentUrl(), course.getThumbnailUrl(), course.getEstimatedDuration()))
                .findFirst();
    }

    @Override
    public Course createCourse(Integer creatorId, CreateCourseDTO createCourseDTO) {
        if(userRepository.findByUserId(creatorId) == null){
            throw new RuntimeException("Creator ID not found!");
        }

        if(domainRepository.findByDomainId(createCourseDTO.getDomainId()) == null){
            throw new RuntimeException("Domain ID not found!");
        }

        if(difficultyLevelRepository.findByLevelId(createCourseDTO.getDifficultyLevelId()) == null){
            throw new RuntimeException("DifficultyLevel ID not found!");
        }


        Course course = courseRepository.save(Course.builder()
                .title(createCourseDTO.getTitle())
                .description(createCourseDTO.getDescription())
                .creator(userRepository.findByUserId(creatorId))
                .domain(domainRepository.findByDomainId(createCourseDTO.getDomainId()))
                .difficultyLevel(difficultyLevelRepository.findByLevelId(createCourseDTO.getDifficultyLevelId()))
                .attachmentUrl(createCourseDTO.getAttachmentUrl())
                .thumbnailUrl(createCourseDTO.getThumbnailUrl())
                .estimatedDuration(createCourseDTO.getEstimatedDuration())
                .isPremium(createCourseDTO.isPremium())
                .build());

        return course;
    }

    @Override
    public void updateCourse(Integer courseId, UpdateCourseDTO updateCourseDTO) {
        if(courseRepository.findByCourseId(courseId) == null){
            throw new RuntimeException("Course ID not found!");
        }

        if(domainRepository.findByDomainId(updateCourseDTO.getDomainId()) == null){
            throw new RuntimeException("Domain ID not found!");
        }

        if(difficultyLevelRepository.findByLevelId(updateCourseDTO.getDifficultyLevelId()) == null){
            throw new RuntimeException("DifficultyLevel ID not found!");
        }

        boolean hasStudents = studentStudyPlanRepository.findAll().stream()
                .anyMatch(s -> s.getCourseStudyPlan().getCourse().getCourseId().equals(courseId)
                            && s.getStatus().equalsIgnoreCase("Active"));

        if (Boolean.FALSE.equals(updateCourseDTO.isPublic()) && hasStudents) {
            throw new RuntimeException("Cannot deactivate Course because students are currently enrolled.");
        }

        Course course = courseRepository.findByCourseId(courseId);
        course.setTitle(updateCourseDTO.getTitle());
        course.setDescription(updateCourseDTO.getDescription());
        course.setDomain(domainRepository.findByDomainId(updateCourseDTO.getDomainId()));
        course.setDifficultyLevel(difficultyLevelRepository.findByLevelId(updateCourseDTO.getDifficultyLevelId()));
        course.setAttachmentUrl(updateCourseDTO.getAttachmentUrl());
        course.setThumbnailUrl(updateCourseDTO.getThumbnailUrl());
        course.setEstimatedDuration(updateCourseDTO.getEstimatedDuration());
        course.setPublic(updateCourseDTO.isPublic());
        course.setPremium(updateCourseDTO.isPremium());
        course.setUpdateAt(LocalDateTime.now());

        courseRepository.save(course);
    }

    @Override
    public List<Course> searchCourseByTitle(String keyword) {
        return courseRepository.findByTitleContainingIgnoreCase(keyword)
                        .stream().filter(Course::isPublic).toList();
    }
}
