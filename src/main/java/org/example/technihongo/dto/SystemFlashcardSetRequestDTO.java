package org.example.technihongo.dto;

import lombok.*;
import org.example.technihongo.enums.DifficultyLevelEnum;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemFlashcardSetRequestDTO {
    private String title;
    private String description;
    private Boolean isPublic;
    private Boolean isPremium;
    private DifficultyLevelEnum difficultyLevel;
}
