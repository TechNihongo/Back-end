package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.CreateLearningPathDTO;
import org.example.technihongo.dto.UpdateLearningPathDTO;
import org.example.technihongo.entities.LearningPath;

import java.util.List;

public interface LearningPathService {
    List<LearningPath> getAllLearningPaths(String keyword, Integer domainId);
    List<LearningPath> getPublicLearningPaths(String keyword, Integer domainId);
    List<LearningPath> getLearningPathsByTitle(String keyword);
    List<LearningPath> getPublicLearningPathsByTitle(String keyword);
    LearningPath getLearningPathById(Integer pathId);
    LearningPath getPublicLearningPathById(Integer pathId);
    LearningPath createLearningPath(Integer creatorId, CreateLearningPathDTO createLearningPathDTO);
    void updateLearningPath(Integer pathId, UpdateLearningPathDTO updateLearningPathDTO);
    void deleteLearningPath(Integer pathId);
    void updateTotalCourses(Integer pathId);
    List<LearningPath> getListLearningPathsByCreatorId(Integer creatorId, String keyword, Integer domainId);
}
