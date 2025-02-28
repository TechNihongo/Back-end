package org.example.technihongo.repositories;

import org.example.technihongo.entities.PathCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PathCourseRepository extends JpaRepository<PathCourse, Integer> {
    List<PathCourse> findByLearningPath_PathId(Integer pathId);
    List<PathCourse> findByLearningPath_PathIdOrderByCourseOrderAsc(Integer pathId);
    int countByLearningPath_PathId(Integer pathId);
}
