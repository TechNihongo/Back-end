package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.LearningResourceDTO;
import org.example.technihongo.dto.LearningResourceStatusDTO;
import org.example.technihongo.entities.LearningResource;

import java.util.List;

public interface LearningResourceService {
    List<LearningResource> getAllLearningResources();
    LearningResource getLearningResourceById(Integer learningResourceId);
    LearningResource getPublicLearningResourceById(Integer userId, Integer learningResourceId);
    LearningResource createLearningResource(Integer creatorId, LearningResourceDTO learningResourceDTO);
    void updateLearningResource(Integer learningResourceId, LearningResourceDTO learningResourceDTO);
    void updateLearningResourceStatus(Integer learningResourceId, LearningResourceStatusDTO learningResourceStatusDTO);
    void deleteLearningResource(Integer learningResourceId);
    List<LearningResource> getListLearningResourcesByCreatorId(Integer creatorId);
}
