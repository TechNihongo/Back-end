package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateQuizDTO {
    private String title;
    private String description;
    private Integer domainId;
    private Integer difficultyLevelId;
    private BigDecimal passingScore;
}
