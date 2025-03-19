package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.entities.Student;
import org.example.technihongo.entities.StudentDailyLearningLog;
import org.example.technihongo.entities.StudentLearningStatistics;
import org.example.technihongo.repositories.StudentDailyLearningLogRepository;
import org.example.technihongo.repositories.StudentLearningStatisticsRepository;
import org.example.technihongo.repositories.StudentRepository;
import org.example.technihongo.services.interfaces.StudentDailyLearningLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Component
public class StudentDailyLearningLogServiceImpl implements StudentDailyLearningLogService {
    @Autowired
    private StudentDailyLearningLogRepository dailyLogRepository;
    @Autowired
    private StudentLearningStatisticsRepository statsRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    @Override
    public void trackStudentDailyLearningLog(Integer studentId, Integer studyTimeInput) {
        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();

        // Tìm student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found!"));

        // Kiểm tra xem log của ngày hôm nay đã tồn tại chưa
        Optional<StudentDailyLearningLog> existingLogOpt = dailyLogRepository
                .findByStudentStudentIdAndLogDate(studentId, today);

        StudentDailyLearningLog dailyLog;
        boolean isNewLog = false; // Biến để kiểm tra log mới
        if (existingLogOpt.isPresent()) {
            // Nếu đã tồn tại log, cập nhật
            dailyLog = existingLogOpt.get();
            updateDailyLog(dailyLog, studyTimeInput, student.getDailyGoal());
        } else {
            // Nếu chưa tồn tại, tạo mới log
            dailyLog = createNewDailyLog(student, today);
            updateDailyLog(dailyLog, studyTimeInput, student.getDailyGoal());
            isNewLog = true; // Đánh dấu là log mới
        }

        // Lưu daily log
        dailyLogRepository.save(dailyLog);

        // Cập nhật StudentLearningStatistics
        updateLearningStatistics(student, dailyLog, isNewLog);
    }

    @Override
    public StudentDailyLearningLog getStudentDailyLearningLog(Integer studentId) {
        return dailyLogRepository.findByStudentStudentIdAndLogDate(studentId, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("Student ID not found!"));
    }

    private StudentDailyLearningLog createNewDailyLog(Student student, LocalDate today) {
        StudentDailyLearningLog dailyLog = new StudentDailyLearningLog();
        dailyLog.setStudent(student);
        dailyLog.setLogDate(today);
        dailyLog.setStudyTime(0);           // Khởi tạo bằng 0
        dailyLog.setCompletedLessons(0);    // Khởi tạo bằng 0
        dailyLog.setCompletedQuizzes(0);    // Khởi tạo bằng 0
        dailyLog.setCompletedResources(0);  // Khởi tạo bằng 0
        dailyLog.setCompletedFlashcardSets(0); // Khởi tạo bằng 0
        dailyLog.setDailyGoalAchieved(false);  // Mặc định là false

        // Tính streak
        int streak = calculateStreak(student, today);
        dailyLog.setStreak(streak);

        return dailyLog;
    }

    private void updateDailyLog(StudentDailyLearningLog dailyLog, Integer studyTimeInput, Integer dailyGoal) {
        // Cộng dồn studyTime
        int newStudyTime = dailyLog.getStudyTime() + studyTimeInput;
        dailyLog.setStudyTime(newStudyTime);

        // Kiểm tra dailyGoalAchieved
        dailyLog.setDailyGoalAchieved(newStudyTime >= dailyGoal);

        // Các trường khác (completedLessons, completedQuizzes, v.v.) có thể được cập nhật qua hàm khác
        // Ở đây giả sử chỉ cập nhật studyTime, các trường khác sẽ được cập nhật riêng nếu cần
    }

    private int calculateStreak(Student student, LocalDate today) {
        // Tìm log của ngày hôm qua
        LocalDate yesterday = today.minusDays(1);
        Optional<StudentDailyLearningLog> yesterdayLogOpt = dailyLogRepository
                .findByStudentStudentIdAndLogDate(student.getStudentId(), yesterday);

        // Nếu có log hôm qua, tăng streak lên 1
        // Nếu không có log hôm qua, bắt đầu streak mới từ 1
        return yesterdayLogOpt.map(studentDailyLearningLog -> studentDailyLearningLog.getStreak() + 1).orElse(1);
    }

    private void updateLearningStatistics(Student student, StudentDailyLearningLog dailyLog, boolean isNewLog) {
        // Tìm hoặc tạo StudentLearningStatistics
        StudentLearningStatistics stats = statsRepository.findByStudentStudentId(student.getStudentId())
                .orElseGet(() -> {
                    StudentLearningStatistics newStats = new StudentLearningStatistics();
                    newStats.setStudent(student);
                    newStats.setTotalStudyTime(0);
                    newStats.setTotalCompletedCourses(0);
                    newStats.setTotalCompletedLessons(0);
                    newStats.setTotalCompletedQuizzes(0);
                    newStats.setActiveDaysCount(0);
                    newStats.setMaxDaysStreak(0);
                    newStats.setTotalAchievementsUnlocked(0);
                    return newStats;
                });

        // Cập nhật totalStudyTime
        stats.setTotalStudyTime(stats.getTotalStudyTime() + dailyLog.getStudyTime());

        // Cập nhật activeDaysCount (chỉ khi log là mới)
        if (isNewLog) {
            stats.setActiveDaysCount(stats.getActiveDaysCount() + 1);
        }

        // Cập nhật maxDaysStreak
        int currentStreak = dailyLog.getStreak();
        if (currentStreak > stats.getMaxDaysStreak()) {
            stats.setMaxDaysStreak(currentStreak);
        }

        // Cập nhật lastStudyDate
        stats.setLastStudyDate(LocalDateTime.now());

        // Lưu statistics
        statsRepository.save(stats);
    }
}
