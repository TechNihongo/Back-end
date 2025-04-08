package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.technihongo.enums.Category;
import org.example.technihongo.enums.ConditionType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AchievementDTO {
    private Integer achievementId;
    private String badgeName;
    private String description;
    private String imageURL;
    private Category category;
    private ConditionType conditionType;
    private Integer conditionValue;
    private boolean isActive;
}
