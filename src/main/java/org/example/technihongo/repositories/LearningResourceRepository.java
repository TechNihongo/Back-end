package org.example.technihongo.repositories;

import org.example.technihongo.entities.LearningResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningResourceRepository extends JpaRepository<LearningResource, Integer> {
    LearningResource findByResourceId(Integer resourceId);
    List<LearningResource> findByCreator_UserId(Integer creatorId);
}
