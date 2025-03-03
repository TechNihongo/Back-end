package org.example.technihongo.repositories;

import org.example.technihongo.entities.SystemFlashcardSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemFlashcardSetRepository extends JpaRepository<SystemFlashcardSet, Integer> {
    List<SystemFlashcardSet> findByCreatorUserId(Integer userId);
    SystemFlashcardSet findBySystemSetId(Integer setId);
    boolean existsByDomainDomainId(Integer domainId);

}
