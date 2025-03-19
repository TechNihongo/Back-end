package org.example.technihongo.repositories;

import org.example.technihongo.entities.LearningPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningPathRepository extends JpaRepository<LearningPath, Integer> {
    LearningPath findByPathId(Integer pathId);
    List<LearningPath> findByTitleContainsIgnoreCaseOrderByCreatedAtDesc(String keyword);
    List<LearningPath> findByCreator_UserIdOrderByCreatedAtDesc(Integer creatorId);
    boolean existsByDomainDomainId(Integer domainId);

    List<LearningPath> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String keyword);
    List<LearningPath> findByDomain_DomainIdOrderByCreatedAtDesc(Integer domainId);
    List<LearningPath> findByTitleContainingIgnoreCaseAndDomain_DomainIdOrderByCreatedAtDesc(String keyword, Integer domainId);

    List<LearningPath> findByTitleContainingIgnoreCaseAndCreator_UserIdOrderByCreatedAtDesc(String keyword, Integer creatorId);
    List<LearningPath> findByDomain_DomainIdAndCreator_UserIdOrderByCreatedAtDesc(Integer domainId, Integer creatorId);
    List<LearningPath> findByTitleContainingIgnoreCaseAndDomain_DomainIdAndCreator_UserIdOrderByCreatedAtDesc(String keyword, Integer domainId, Integer creatorId);
}
