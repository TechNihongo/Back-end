package org.example.technihongo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlashcardProgressDTO {
    private Integer flashcardId;
    private String japaneseDefinition;
    private String vietEngTranslation;
    private boolean isLearned;
    private LocalDateTime lastStudied;
    private boolean starred;
    private String setType;
}
