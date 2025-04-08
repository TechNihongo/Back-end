package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.StudentAchievementDTO;
import org.example.technihongo.entities.*;
import org.example.technihongo.enums.ActivityType;
import org.example.technihongo.enums.Category;
import org.example.technihongo.enums.ConditionType;
import org.example.technihongo.repositories.*;
import org.example.technihongo.services.interfaces.AchievementService;
import org.example.technihongo.services.interfaces.StudentFlashcardSetProgressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {

    private final AchievementRepository achievementRepository;
    private final StudentAchievementRepository studentAchievementRepository;
    private final StudentAchievementProgressRepository progressRepository;
    private final StudentDailyLearningLogRepository dailyLearningLogRepository;
    private final UserActivityLogRepository userActivityLogRepository;
    private final StudentFlashcardSetProgressService flashcardSetProgressService;
    private final StudentFlashcardSetProgressRepository setProgressRepo;

    private static final LocalTime NIGHT_OWL_TIME = LocalTime.of(22, 30);
    private static final LocalTime EARLY_BIRD_TIME = LocalTime.of(7, 30);

    @Override
    public List<Achievement> achievementList() {
        return achievementRepository.findAll();
    }

    private boolean lacksAchievement(Integer studentId, Integer achievementId) {
        return studentAchievementRepository
                .findByStudent_StudentIdAndAchievement_AchievementId(studentId, achievementId)
                .isEmpty();
    }

    @Override
    public void assignAchievementsToStudent(Integer studentId, LocalDateTime loginTime) {
        Optional<Achievement> nightOwlOpt = achievementRepository.findByBadgeName("Cú đêm thức khuya");
        if (nightOwlOpt.isPresent()) {
            Achievement nightOwl = nightOwlOpt.get();
            if (lacksAchievement(studentId, nightOwl.getAchievementId()) && isNightOwlTime(loginTime)) {
                awardAchievement(studentId, nightOwl.getAchievementId());
            }
        }

        Optional<Achievement> earlyBirdOpt = achievementRepository.findByBadgeName("Chim sâu dậy sớm");
        if (earlyBirdOpt.isPresent()) {
            Achievement earlyBird = earlyBirdOpt.get();
            if (lacksAchievement(studentId, earlyBird.getAchievementId()) && isEarlyBirdTime(loginTime)) {
                awardAchievement(studentId, earlyBird.getAchievementId());
            }
        }
    }

    @Override
    public void trackAchievementProgress(Integer studentId) {
        Optional<UserActivityLog> lastLoginOpt = userActivityLogRepository
                .findTopByUser_UserIdAndActivityTypeOrderByCreatedAtDesc(studentId, ActivityType.LOGIN);
        if (lastLoginOpt.isPresent()) {
            LocalDateTime loginTime = lastLoginOpt.get().getCreatedAt();
            assignAchievementsToStudent(studentId, loginTime);
        }
    }

    @Override
    public StudentAchievementDTO awardAchievement(Integer studentId, Integer achievementId) {
        StudentAchievement studentAchievement = StudentAchievement.builder()
                .student(Student.builder().studentId(studentId).build())
                .achievement(Achievement.builder().achievementId(achievementId).build())
                .achievedAt(LocalDateTime.now())
                .build();

        StudentAchievement saved = studentAchievementRepository.save(studentAchievement);
        return new StudentAchievementDTO(
                saved.getStudentAchievementId(),
                saved.getStudent().getStudentId(),
                saved.getAchievement().getAchievementId(),
                saved.getAchievedAt()
        );
    }

    @Override
    public void checkAndAssignStreakAchievements(Integer studentId) {
        Optional<StudentDailyLearningLog> latestLogOpt = dailyLearningLogRepository
                .findTopByStudent_StudentIdOrderByLogDateDesc(studentId);
        if (latestLogOpt.isEmpty() || latestLogOpt.get().getStudyTime() <= 0) {
            return;
        }

        int currentStreak = latestLogOpt.get().getStreak();
        List<Achievement> streakAchievements = achievementRepository.findAll().stream()
                .filter(a -> a.getCategory() == Category.Streak && a.getConditionType() == ConditionType.DAYS_STREAK)
                .toList();

        for (Achievement achievement : streakAchievements) {
            Optional<StudentAchievementProgress> progressOpt = progressRepository
                    .findByStudent_StudentIdAndAchievement_AchievementId(studentId, achievement.getAchievementId());

            StudentAchievementProgress progress;
            if (progressOpt.isEmpty()) {
                progress = StudentAchievementProgress.builder()
                        .student(Student.builder().studentId(studentId).build())
                        .achievement(achievement)
                        .currentValue(currentStreak)
                        .requiredValue(achievement.getConditionValue())
                        .lastUpdated(LocalDateTime.now())
                        .build();
            } else {
                progress = progressOpt.get();
                progress.setCurrentValue(currentStreak);
                progress.setLastUpdated(LocalDateTime.now());
            }

            progressRepository.save(progress);

            if (lacksAchievement(studentId, achievement.getAchievementId()) && progress.getCurrentValue() >= progress.getRequiredValue()) {
                awardAchievement(studentId, achievement.getAchievementId());
            }
        }
    }

    @Override
    public void checkAndAssignFlashcardAchievement(Integer studentId, Integer studentSetId) {
        Optional<Achievement> flashcardAchievementOpt = achievementRepository.findByBadgeName("Tập sự ghép thẻ");
        if (flashcardAchievementOpt.isEmpty()) {
            return;
        }

        Achievement flashcardAchievement = flashcardAchievementOpt.get();

        if (lacksAchievement(studentId, flashcardAchievement.getAchievementId())) {
            flashcardSetProgressService.trackFlashcardSetProgress(studentId, studentSetId, false, null);

            Optional<StudentFlashcardSetProgress> progressOpt = setProgressRepo.findByStudentStudentIdAndStudentFlashcardSet_StudentSetId(studentId, studentSetId);


            if (progressOpt.isPresent()) {
                StudentFlashcardSetProgress progress = progressOpt.get();
                // Chỉ trao thưởng khi đã học ít nhất 1 flashcard (card_studied > 0)
                if (progress.getCardStudied() > 0) {
                    awardAchievement(studentId, flashcardAchievement.getAchievementId());
                }
            }
        }
    }

    private boolean isNightOwlTime(LocalDateTime loginTime) {
        LocalTime time = loginTime.toLocalTime();
        return time.isAfter(NIGHT_OWL_TIME) || time.equals(NIGHT_OWL_TIME);
    }

    private boolean isEarlyBirdTime(LocalDateTime loginTime) {
        LocalTime time = loginTime.toLocalTime();
        return time.isBefore(EARLY_BIRD_TIME.plusMinutes(30)) && time.isAfter(EARLY_BIRD_TIME.minusMinutes(30));
    }
}
