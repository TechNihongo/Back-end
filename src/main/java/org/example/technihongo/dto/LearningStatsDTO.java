package org.example.technihongo.dto;

import lombok.Data;

import java.util.List;

@Data
public class LearningStatsDTO {
    private List<DailyStatsDTO> weeklyStats;
    private List<WeeklyStatsDTO> monthlyStats;
    private List<MonthlyStatsDTO> yearlyStats;

    @Data
    public static class DailyStatsDTO {
        private String date;
        private Integer studyTime;
        private Integer completedLessons;
        private Integer completedQuizzes;
        private Integer completedResources;
        private Integer completedFlashcardSets;
        private boolean dailyGoalAchieved;

        public DailyStatsDTO(String date, Integer studyTime, Integer completedLessons,
                             Integer completedQuizzes, Integer completedResources,
                             Integer completedFlashcardSets, boolean dailyGoalAchieved) {
            this.date = date;
            this.studyTime = studyTime != null ? studyTime : 0;
            this.completedLessons = completedLessons != null ? completedLessons : 0;
            this.completedQuizzes = completedQuizzes != null ? completedQuizzes : 0;
            this.completedResources = completedResources != null ? completedResources : 0;
            this.completedFlashcardSets = completedFlashcardSets != null ? completedFlashcardSets : 0;
            this.dailyGoalAchieved = dailyGoalAchieved;
        }
    }

    @Data
    public static class WeeklyStatsDTO {
        private String week;
        private Integer studyTime;
        private Integer completedLessons;
        private Integer completedQuizzes;
        private Integer completedResources;
        private Integer completedFlashcardSets;

        public WeeklyStatsDTO(String week, Integer studyTime, Integer completedLessons,
                              Integer completedQuizzes, Integer completedResources,
                              Integer completedFlashcardSets) {
            this.week = week;
            this.studyTime = studyTime != null ? studyTime : 0;
            this.completedLessons = completedLessons != null ? completedLessons : 0;
            this.completedQuizzes = completedQuizzes != null ? completedQuizzes : 0;
            this.completedResources = completedResources != null ? completedResources : 0;
            this.completedFlashcardSets = completedFlashcardSets != null ? completedFlashcardSets : 0;
        }
    }

    @Data
    public static class MonthlyStatsDTO {
        private String month;
        private Integer studyTime;
        private Integer completedLessons;
        private Integer completedQuizzes;
        private Integer completedResources;
        private Integer completedFlashcardSets;

        public MonthlyStatsDTO(String month, Integer studyTime, Integer completedLessons,
                               Integer completedQuizzes, Integer completedResources,
                               Integer completedFlashcardSets) {
            this.month = month;
            this.studyTime = studyTime != null ? studyTime : 0;
            this.completedLessons = completedLessons != null ? completedLessons : 0;
            this.completedQuizzes = completedQuizzes != null ? completedQuizzes : 0;
            this.completedResources = completedResources != null ? completedResources : 0;
            this.completedFlashcardSets = completedFlashcardSets != null ? completedFlashcardSets : 0;
        }
    }
}
