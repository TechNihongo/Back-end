package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateFlashcardSetFromResourceDTO {
    private Integer resourceId;
    private String title;
    private String description;
    private Boolean isPublic;
    private List<FlashcardRequestDTO> flashcards;
}
