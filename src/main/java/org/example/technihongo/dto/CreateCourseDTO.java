package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCourseDTO {
    private Integer courseId;
    private String title;
    private String description;
    private Integer creatorId;
    private Integer domainId;
    private Integer difficultyLevelId;
    private String attachmentUrl;
    private String thumbnailUrl;
    private String estimatedDuration;
    private boolean isPremium;
}
