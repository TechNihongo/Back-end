package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.PageResponseDTO;
import org.example.technihongo.entities.LearningResource;
import org.example.technihongo.entities.StudentFavorite;

public interface StudentFavoriteService {
    StudentFavorite saveLearningResource(Integer studentId, Integer learningResourceId);
    PageResponseDTO<LearningResource> getListFavoriteLearningResourcesByStudentId(
            Integer studentId, int pageNo, int pageSize, String sortBy, String sortDir);
}
