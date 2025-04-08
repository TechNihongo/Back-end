package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyPlanRepository extends JpaRepository<StudyPlan, Integer> {
    StudyPlan findByStudyPlanId(Integer id);
    List<StudyPlan> findByCourse_CourseId(Integer courseId);
    Integer countByCourse_CourseId(Integer courseId);
    List<StudyPlan> findByActiveTrue();

    @Query("SELECT sp FROM StudyPlan sp WHERE sp.course.courseId = :courseId AND sp.isDefault = :isDefault")
    Optional<StudyPlan> findByCourse_CourseIdAndDefault(
            @Param("courseId") Integer courseId,
            @Param("isDefault") boolean isDefault);

//    @Query("SELECT sp FROM StudyPlan sp WHERE sp.active = true AND sp.isDefault = true")
//    List<StudyPlan> findDefaultActivePlans();
}

