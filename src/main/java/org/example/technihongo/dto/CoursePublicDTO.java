package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.technihongo.entities.DifficultyLevel;
import org.example.technihongo.entities.Domain;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoursePublicDTO {
    private Integer courseId;
    private String title;
    private String description;
    private Domain domain;
    private DifficultyLevel difficultyLevel;
//    private String attachmentUrl;
    private String thumbnailUrl;
    private String estimatedDuration;
}
