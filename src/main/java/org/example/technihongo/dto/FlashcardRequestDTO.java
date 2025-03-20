package org.example.technihongo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlashcardRequestDTO {
    private String japaneseDefinition;
    private String vietEngTranslation;
    private String imageUrl;
    private String vocabOrder;
}
