package org.example.technihongo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FlashcardSetResponseDTO {
    private Integer studentId;
    private Integer studentSetId;
    private String userName;
    private String profileImg;
    private String title;
    private String description;
    private Integer totalViews;
    private Boolean isPublic;
    private Boolean isViolated;
    private List<FlashcardResponseDTO> flashcards;
    private LocalDateTime createdAt;
}
