package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentDailyLearningLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentDailyLearningLogRepository extends JpaRepository<StudentDailyLearningLog, Integer> {
    Optional<StudentDailyLearningLog> findByStudentStudentIdAndLogDate(Integer studentId, LocalDate logDate);
    Optional<StudentDailyLearningLog> findTopByStudent_StudentIdOrderByLogDateDesc(Integer studentId);

    @Query("SELECT l FROM StudentDailyLearningLog l WHERE l.student.studentId = :studentId AND l.logDate >= :startDate")
    List<StudentDailyLearningLog> findByStudentIdAndDateRange(
            @Param("studentId") Integer studentId,
            @Param("startDate") LocalDate startDate
    );

    @Query(value = "SELECT CAST(l.log_date AS DATE) AS logDay, " +
            "SUM(l.study_time) AS totalStudyTime, " +
            "SUM(l.completed_lessons) AS totalLessons, " +
            "SUM(l.completed_quizzes) AS totalQuizzes, " +
            "SUM(l.completed_resources) AS totalResources, " +
            "SUM(l.completed_flashcard_sets) AS totalFlashcards " +
            "FROM StudentDailyLearningLog l " +
            "WHERE l.student_id = :studentId AND l.log_date >= :startDate " +
            "GROUP BY CAST(l.log_date AS DATE)", nativeQuery = true)
    List<Object[]> sumByDay(
            @Param("studentId") Integer studentId,
            @Param("startDate") LocalDate startDate
    );

    @Query(value = "SELECT DATEADD(DAY, -DATEPART(WEEKDAY, l.log_date) + 1, CAST(l.log_date AS DATE)) AS weekStart, " +
            "SUM(l.study_time) AS totalStudyTime, " +
            "SUM(l.completed_lessons) AS totalLessons, " +
            "SUM(l.completed_quizzes) AS totalQuizzes, " +
            "SUM(l.completed_resources) AS totalResources, " +
            "SUM(l.completed_flashcard_sets) AS totalFlashcards " +
            "FROM StudentDailyLearningLog l " +
            "WHERE l.student_id = :studentId AND l.log_date >= :startDate " +
            "GROUP BY DATEADD(DAY, -DATEPART(WEEKDAY, l.log_date) + 1, CAST(l.log_date AS DATE))", nativeQuery = true)
    List<Object[]> sumByWeek(
            @Param("studentId") Integer studentId,
            @Param("startDate") LocalDate startDate
    );

    @Query(value = "SELECT DATEADD(MONTH, DATEDIFF(MONTH, 0, l.log_date), 0) AS monthStart, " +
            "SUM(l.study_time) AS totalStudyTime, " +
            "SUM(l.completed_lessons) AS totalLessons, " +
            "SUM(l.completed_quizzes) AS totalQuizzes, " +
            "SUM(l.completed_resources) AS totalResources, " +
            "SUM(l.completed_flashcard_sets) AS totalFlashcards " +
            "FROM StudentDailyLearningLog l " +
            "WHERE l.student_id = :studentId AND l.log_date >= :startDate " +
            "GROUP BY DATEADD(MONTH, DATEDIFF(MONTH, 0, l.log_date), 0)", nativeQuery = true)
    List<Object[]> sumByMonth(
            @Param("studentId") Integer studentId,
            @Param("startDate") LocalDate startDate
    );
}
