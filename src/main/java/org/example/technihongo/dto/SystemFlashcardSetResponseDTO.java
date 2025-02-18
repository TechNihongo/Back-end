package org.example.technihongo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SystemFlashcardSetResponseDTO {
    private Integer contentManagerId;
    private String title;
    private String description;
    private Boolean isPublic;
    private List<FlashcardResponseDTO> flashcards;
}
