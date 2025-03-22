package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentLessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentLessonProgressRepository extends JpaRepository<StudentLessonProgress, Integer> {
    Optional<StudentLessonProgress> findByStudentStudentIdAndLessonLessonId(Integer studentId, Integer lessonId);
    List<StudentLessonProgress> findByStudentStudentIdAndLesson_StudyPlanStudyPlanId(Integer studentId, Integer studyPlanId);
}
