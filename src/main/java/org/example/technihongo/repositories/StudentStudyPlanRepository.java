package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentStudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentStudyPlanRepository extends JpaRepository<StudentStudyPlan, Integer> {
    boolean existsByStudyPlan_StudyPlanId(Integer studyPlanId);

    List<StudentStudyPlan> findByStudentStudentId(Integer studentId);

    @Query("SELECT ssp FROM StudentStudyPlan ssp WHERE ssp.student.studentId = :studentId AND ssp.status = 'ACTIVE'")
    Optional<StudentStudyPlan> findActiveStudyPlanByStudentId(@Param("studentId") Integer studentId);

    @Query("SELECT ssp FROM StudentStudyPlan ssp WHERE ssp.student.studentId = :studentId AND ssp.studyPlan.studyPlanId = :studyPlanId")
    Optional<StudentStudyPlan> findByStudentIdAndStudyPlanId(
            @Param("studentId") Integer studentId,
            @Param("studyPlanId") Integer studyPlanId
    );

    @Query("SELECT ssp FROM StudentStudyPlan ssp WHERE ssp.student.studentId = :studentId AND ssp.studyPlan.course.courseId = :courseId AND ssp.status = 'ACTIVE'")
    Optional<StudentStudyPlan> findActiveStudyPlanByStudentIdAndCourseId(
            @Param("studentId") Integer studentId,
            @Param("courseId") Integer courseId
    );

//    @Query("SELECT COUNT(ssp) > 0 FROM StudentStudyPlan ssp WHERE ssp.student.studentId = :studentId AND ssp.status = 'Active'")
//    boolean hasActiveStudyPlan(@Param("studentId") Integer studentId);
}
