package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CreatePathCourseDTO;
import org.example.technihongo.dto.UpdatePathCourseOrderDTO;
import org.example.technihongo.entities.Lesson;
import org.example.technihongo.entities.PathCourse;
import org.example.technihongo.repositories.CourseRepository;
import org.example.technihongo.repositories.LearningPathRepository;
import org.example.technihongo.repositories.PathCourseRepository;
import org.example.technihongo.services.interfaces.PathCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Component
public class PathCourseServiceImpl implements PathCourseService {
    @Autowired
    private PathCourseRepository pathCourseRepository;
    @Autowired
    private LearningPathRepository learningPathRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<PathCourse> getPathCoursesByLearningPathId(Integer pathId) {
        if(learningPathRepository.findByPathId(pathId) == null){
            throw new RuntimeException("LearningPath ID not found!");
        }
        return pathCourseRepository.findByLearningPath_PathIdOrderByCourseOrderAsc(pathId);
    }

    @Override
    public PathCourse getPathCourseById(Integer pathCourseId) {
        return pathCourseRepository.findById(pathCourseId)
                .orElseThrow(() -> new RuntimeException("PathCourse ID not found"));
    }

    @Override
    public PathCourse createPathCourse(CreatePathCourseDTO createPathCourseDTO) {
        if(learningPathRepository.findByPathId(createPathCourseDTO.getPathId()) == null){
            throw new RuntimeException("LearningPath ID not found!");
        }

        if(courseRepository.findByCourseId(createPathCourseDTO.getCourseId()) == null){
            throw new RuntimeException("Course ID not found!");
        }

        PathCourse pathCourse = pathCourseRepository.save(PathCourse.builder()
                .learningPath(learningPathRepository.findByPathId(createPathCourseDTO.getPathId()))
                .course(courseRepository.findByCourseId(createPathCourseDTO.getCourseId()))
                .courseOrder(pathCourseRepository.countByLearningPath_PathId(createPathCourseDTO.getPathId()) + 1)
                .build());

        return pathCourse;
    }

    @Override
    public void updatePathCourseOrder(Integer pathId, UpdatePathCourseOrderDTO updatePathCourseOrderDTO) {
        if(learningPathRepository.findByPathId(pathId) == null){
            throw new RuntimeException("LearningPath ID not found!");
        }

        List<PathCourse> pathCourses = pathCourseRepository.findByLearningPath_PathIdOrderByCourseOrderAsc(pathId);
        List<Integer> newOrder = updatePathCourseOrderDTO.getNewPathCourseOrder();

        if (pathCourses.size() != newOrder.size()) {
            throw new IllegalArgumentException("PathCourse count does not match newOrder!");
        }

        for (int i = 0; i < pathCourses.size(); i++) {
            pathCourses.get(i).setCourseOrder(newOrder.get(i));
        }

        pathCourseRepository.saveAll(pathCourses);
    }

    @Override
    @Transactional
    public void deletePathCourse(Integer pathCourseId) {
        PathCourse deletedPathCourse = pathCourseRepository.findById(pathCourseId)
                .orElseThrow(() -> new RuntimeException("PathCourse ID not found"));

        Integer pathId = deletedPathCourse.getLearningPath().getPathId();
        Integer deletedOrder = deletedPathCourse.getCourseOrder();

        pathCourseRepository.delete(deletedPathCourse);

        List<PathCourse> pathCourses = pathCourseRepository.findByLearningPath_PathIdOrderByCourseOrderAsc(pathId);
        for (PathCourse pathCourse : pathCourses) {
            if (pathCourse.getCourseOrder() > deletedOrder) {
                pathCourse.setCourseOrder(pathCourse.getCourseOrder() - 1);
            }
        }

        pathCourseRepository.saveAll(pathCourses);
    }
}
