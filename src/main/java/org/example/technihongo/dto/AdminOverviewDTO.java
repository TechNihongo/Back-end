package org.example.technihongo.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdminOverviewDTO {
    private Long totalStudents;
    private Long totalActiveCourses;
    private Long totalSubscriptionsSold;
    private List<DailyRevenueDTO> weeklyRevenue;
    private List<WeeklyRevenueDTO> monthlyRevenue;
    private List<MonthlyRevenueDTO> yearlyRevenue;

    @Data
    public static class DailyRevenueDTO {
        private String date;
        private Double revenue;

        public DailyRevenueDTO(String date, Double revenue) {
            this.date = date;
            this.revenue = revenue != null ? revenue : 0.0;
        }
    }

    @Data
    public static class WeeklyRevenueDTO {
        private String week;
        private Double revenue;

        public WeeklyRevenueDTO(String week, Double revenue) {
            this.week = week;
            this.revenue = revenue != null ? revenue : 0.0;
        }
    }

    @Data
    public static class MonthlyRevenueDTO {
        private String month;
        private Double revenue;

        public MonthlyRevenueDTO(String month, Double revenue) {
            this.month = month;
            this.revenue = revenue != null ? revenue : 0.0;
        }
    }
}
