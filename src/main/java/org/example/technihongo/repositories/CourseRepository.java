package org.example.technihongo.repositories;

import org.example.technihongo.entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    Course findByCourseId(Integer courseId);
    List<Course> findByTitleContainingIgnoreCase(String keyword);
    List<Course> findByCreator_UserId(Integer creatorId);
    boolean existsByDomainDomainId(Integer domainId);

    Page<Course> findCoursesByPublicStatus(boolean isPublic, Pageable pageable);
    Page<Course> findByCreator_UserId(Integer creatorId, Pageable pageable);
    Page<Course> findByTitleContainingIgnoreCaseAndPublicStatus(String keyword, Boolean isPublic, Pageable pageable);

    Page<Course> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Course> findByTitleContainingIgnoreCaseAndCreator_UserId(String keyword, Integer creatorId, Pageable pageable);
    Page<Course> findByDomain_DomainId(Integer domainId, Pageable pageable);
    Page<Course> findByTitleContainingIgnoreCaseAndDomain_DomainId(String keyword, Integer domainId, Pageable pageable);

    Page<Course> findByDomain_DomainIdAndPublicStatus(Integer domainId, Boolean isPublic, Pageable pageable);
    Page<Course> findByTitleContainingIgnoreCaseAndPublicStatusAndDomain_DomainId(String keyword, Boolean isPublic, Integer domainId, Pageable pageable);
    Page<Course> findByDomain_DomainIdAndCreator_UserId(Integer domainId, Integer creatorId, Pageable pageable);
    Page<Course> findByDomain_DomainIdAndCreator_UserIdAndTitleContainingIgnoreCase(Integer domainId, Integer creatorId, String keyword, Pageable pageable);

    Page<Course> findByDifficultyLevel_LevelId(Integer difficultyLevelId, Pageable pageable);
    Page<Course> findByTitleContainingIgnoreCaseAndDifficultyLevel_LevelId(String keyword, Integer difficultyLevelId, Pageable pageable);
    Page<Course> findByDomain_DomainIdAndDifficultyLevel_LevelId(Integer domainId, Integer difficultyLevelId, Pageable pageable);
    Page<Course> findByTitleContainingIgnoreCaseAndDomain_DomainIdAndDifficultyLevel_LevelId(String keyword, Integer domainId, Integer difficultyLevelId, Pageable pageable);

    Page<Course> findByPublicStatusAndDifficultyLevel_LevelId(Boolean isPublic, Integer difficultyLevelId, Pageable pageable);
    Page<Course> findByTitleContainingIgnoreCaseAndPublicStatusAndDifficultyLevel_LevelId(String keyword, Boolean isPublic, Integer difficultyLevelId, Pageable pageable);
    Page<Course> findByDomain_DomainIdAndPublicStatusAndDifficultyLevel_LevelId(Integer domainId, Boolean isPublic, Integer difficultyLevelId, Pageable pageable);
    Page<Course> findByTitleContainingIgnoreCaseAndPublicStatusAndDomain_DomainIdAndDifficultyLevel_LevelId(String keyword, Boolean isPublic, Integer domainId, Integer difficultyLevelId, Pageable pageable);

    Page<Course> findByCreator_UserIdAndDifficultyLevel_LevelId(Integer creatorId, Integer difficultyLevelId, Pageable pageable);
    Page<Course> findByTitleContainingIgnoreCaseAndCreator_UserIdAndDifficultyLevel_LevelId(String keyword, Integer creatorId, Integer difficultyLevelId, Pageable pageable);
    Page<Course> findByDomain_DomainIdAndCreator_UserIdAndDifficultyLevel_LevelId(Integer domainId, Integer creatorId, Integer difficultyLevelId, Pageable pageable);
    Page<Course> findByDomain_DomainIdAndCreator_UserIdAndTitleContainingIgnoreCaseAndDifficultyLevel_LevelId(Integer domainId, Integer creatorId, String keyword, Integer difficultyLevelId, Pageable pageable);

    Page<Course> findByDomain_DomainIdIn(List<Integer> subDomainIds, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Course c WHERE c.publicStatus = true OR c.enrollmentCount > 0")
    Long countActiveCourses();
}
