package org.example.technihongo.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseStatisticsDTO {
    private Integer courseId;
    private String title;
    private String description;
    private String domainName;
    private String difficultyLevelTag;
    private String estimatedDuration;
    private Integer enrollmentCount;
    private boolean publicStatus;
    private boolean isPremium;
    private LocalDateTime createdAt;
    private Integer completedCount;
    private BigDecimal averageCompletionPercentage;
    private BigDecimal completedPercentage;
}
