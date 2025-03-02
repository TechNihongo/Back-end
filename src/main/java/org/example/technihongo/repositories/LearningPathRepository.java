package org.example.technihongo.repositories;

import org.example.technihongo.entities.Course;
import org.example.technihongo.entities.LearningPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningPathRepository extends JpaRepository<LearningPath, Integer> {
    LearningPath findByPathId(Integer pathId);
    List<LearningPath> findByTitleContainsIgnoreCase(String keyword);
    List<LearningPath> findByCreator_UserId(Integer creatorId);
}
