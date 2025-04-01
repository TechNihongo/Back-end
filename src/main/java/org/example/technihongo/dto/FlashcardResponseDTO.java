package org.example.technihongo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlashcardResponseDTO {
    private Integer flashcardId;
    private String japaneseDefinition;
    private String vietEngTranslation;
    private Integer cardOrder;
    private String imageUrl;
}
