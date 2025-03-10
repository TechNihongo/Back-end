package org.example.technihongo.repositories;

import org.example.technihongo.entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
