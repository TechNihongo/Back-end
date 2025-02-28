package org.example.technihongo.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.technihongo.enums.DifficultyLevelEnum;

import java.util.List;

@Getter
@Setter

public class SystemFlashcardSetResponseDTO {
    private Integer contentManagerId;
    private String title;
    private String description;
    private Boolean isPublic;
    private Boolean isPremium;
    private Integer domainId;
    private DifficultyLevelEnum difficultyLevel;
    private List<FlashcardResponseDTO> flashcards;
}
