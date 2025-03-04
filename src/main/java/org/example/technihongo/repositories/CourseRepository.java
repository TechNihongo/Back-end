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

    Page<Course> findCoursesByPublicIs(boolean isPublic, Pageable pageable);
    Page<Course> findByCreator_UserId(Integer creatorId, Pageable pageable);
    Page<Course> findByTitleContainingIgnoreCaseAndPublic(String keyword, Boolean isPublic, Pageable pageable);
}
