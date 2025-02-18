package org.example.technihongo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlashcardSetRequestDTO {
    private String title;
    private String description;
    private Boolean isPublic;
}
