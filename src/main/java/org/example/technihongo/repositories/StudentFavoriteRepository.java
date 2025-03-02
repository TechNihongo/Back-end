package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentFavoriteRepository extends JpaRepository<StudentFavorite, Integer>{
    Boolean existsByLearningResource_ResourceId(Integer learningResourceId);
}
