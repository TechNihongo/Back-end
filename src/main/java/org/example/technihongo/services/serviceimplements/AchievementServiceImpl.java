package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.StudentAchievementDTO;
import org.example.technihongo.entities.Achievement;
import org.example.technihongo.entities.Student;
import org.example.technihongo.entities.StudentAchievement;
import org.example.technihongo.entities.UserActivityLog;
import org.example.technihongo.enums.ActivityType;
import org.example.technihongo.repositories.AchievementRepository;
import org.example.technihongo.repositories.StudentAchievementProgressRepository;
import org.example.technihongo.repositories.StudentAchievementRepository;
import org.example.technihongo.repositories.UserActivityLogRepository;
import org.example.technihongo.services.interfaces.AchievementService;
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
    private final UserActivityLogRepository userActivityLogRepository;

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
    private boolean isNightOwlTime(LocalDateTime loginTime) {
        LocalTime time = loginTime.toLocalTime();
        return time.isAfter(NIGHT_OWL_TIME) || time.equals(NIGHT_OWL_TIME);
    }

    private boolean isEarlyBirdTime(LocalDateTime loginTime) {
        LocalTime time = loginTime.toLocalTime();
        return time.isBefore(EARLY_BIRD_TIME.plusMinutes(30)) && time.isAfter(EARLY_BIRD_TIME.minusMinutes(30));
    }
}
