package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentResourceProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentResourceProgressRepository extends JpaRepository<StudentResourceProgress, Integer> {
    Boolean existsByLearningResource_ResourceId(Integer learningResourceId);
}
